package com.example.recipes.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.DI
import com.example.recipes.R
import com.example.recipes.databinding.HomeFragmentBinding
import com.example.recipes.ui.details.DetailsFragment
import com.example.recipes.ui.home.recycler.RecipeHomeAdapter
import com.example.recipes.ui.home.recycler.RecipeMarginDecorator
import com.example.recipes.utils.*
import com.example.recipes.utils.filterparameters.getSearchType
import com.example.recipes.utils.filterparameters.getSortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

class HomeFragment : Fragment(), RecipeHomeAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
        private const val SEARCH_VIEW_DEBOUNCE = 300L
        private const val SEARCH_VIEW_QUERY_KEY = "SEARCH_VIEW_QUERY_KEY"
    }

    private val component by lazy { DI.appComponent.plusFragmentComponent().create() }
    private val viewModel by viewModels<HomeViewModel> { component.viewModelFactory() }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val recyclerAdapter = RecipeHomeAdapter(this)
    private var searchQuery: CharSequence? = null
    private lateinit var searchView: SearchView
    private var scrollToTopFlag = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbHome)
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null)
            searchQuery = savedInstanceState.getCharSequence(SEARCH_VIEW_QUERY_KEY)

        setupSearchTypeSpinner()
        setupSortTypeSpinner()
        setupRetryButton()
        setupRecycler()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.stateLD.observe(viewLifecycleOwner, { updateLoadingState(it) })
        viewModel.recipesLD.observe(viewLifecycleOwner, {
            recyclerAdapter.submitList(it) {
                if (scrollToTopFlag) {
                    binding.rvHome.scrollToPosition(0)
                    scrollToTopFlag = false
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecycler() {
        binding.rvHome.apply {
            addItemDecoration(RecipeMarginDecorator(resources.getDimensionPixelSize(R.dimen.margin_small)))
            setOnTouchListener { _, _ -> hideKeyboard(requireActivity()) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerAdapter
        }
    }

    private fun setupSearchTypeSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.searching_type,
            R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnSearchOver.adapter = it
        }
        binding.spnSearchOver.setFakeSelectSkipWatcher({
            viewModel.setSearchSpinnerState(getSearchType(it))
            scrollToTopFlag = true
        })
    }

    private fun setupSortTypeSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_type,
            R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnSort.adapter = it
        }
        binding.spnSort.setFakeSelectSkipWatcher({
            viewModel.setSortSpinnerState(getSortType(it))
            scrollToTopFlag = true
        })
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = getString(R.string.query_hint)
        searchQuery?.let { searchView.setQuery(searchQuery, false) }
        launchFlowSearchView()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun launchFlowSearchView() {
        lifecycleScope.launchWhenResumed {
            searchView.searchWatcherFlow()
                .debounce(SEARCH_VIEW_DEBOUNCE)
                .buffer(Channel.CONFLATED)
                .collect {
                    viewModel.setSearchQuery(it)
                    if (it.length >= SEARCH_MIN_QUERY) scrollToTopFlag = true
                }
        }
    }

    private fun updateLoadingState(state: State) = when (state) {
        is State.Default -> setLoading(false)
        is State.Loading -> setLoading(true)
        is State.Error -> showErrorMessage(state.errorMessage)
        is State.Success -> setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) = with(binding) {
        pbHome.isVisible = isLoading
        tvHomeErrorMessage.isVisible = false
        btnHomeErrorRetry.isVisible = false
    }

    private fun showErrorMessage(message: String) = with(binding) {
        pbHome.isVisible = false
        btnHomeErrorRetry.isVisible = true
        tvHomeErrorMessage.isVisible = true
        tvHomeErrorMessage.text = message
    }

    private fun setupRetryButton() {
        binding.btnHomeErrorRetry.setOnClickListener { viewModel.getRecipes() }
    }


    override fun onRecipeItemClick(uuid: String) {
        hideKeyboard(requireActivity())
        parentFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, DetailsFragment.newInstance(uuid))
            .addToBackStack(null)
            .commit()
    }

    override fun onStop() {
        super.onStop()
        searchQuery = searchView.query
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_VIEW_QUERY_KEY, searchQuery)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}