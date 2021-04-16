package com.example.recipes.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.databinding.HomeFragmentBinding
import com.example.recipes.ui.details.DetailsFragment
import com.example.recipes.utils.State
import com.example.recipes.utils.hideKeyboard
import com.example.recipes.utils.searchWatcherFlow
import com.example.recipes.utils.setFirstSelectSkipWatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

@AndroidEntryPoint
class HomeFragment : Fragment(), RecipeHomeAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
        private const val SEARCH_VIEW_DEBOUNCE = 300L
        private const val SEARCH_VIEW_QUERY_KEY = "SEARCH_VIEW_QUERY_KEY"
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var searchView: SearchView
    private val recyclerAdapter = RecipeHomeAdapter(this)
    private var searchQuery: CharSequence? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            recyclerAdapter.submitList(it) { binding.rvHome.scrollToPosition(0) }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecycler() {
        binding.rvHome.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvHome.adapter = recyclerAdapter

        binding.rvHome.setOnTouchListener { _, _ ->
            hideKeyboard(requireActivity())
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
        binding.spnSearchOver.setFirstSelectSkipWatcher { viewModel.setSearchSpinnerState(it) }
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
        binding.spnSort.setFirstSelectSkipWatcher { viewModel.setSortSpinnerState(it) }
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
                .collect { viewModel.setSearchQuery(it) }
        }
    }

    private fun updateLoadingState(state: State) = when (state) {
        is State.Default -> setLoading(false)
        is State.Loading -> setLoading(true)
        is State.Error -> showErrorMessage(state.errorMessage)
        is State.Success -> setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.pbHome.isVisible = isLoading
        binding.tvHomeErrorMessage.isVisible = false
        binding.btnHomeErrorRetry.isVisible = false
    }

    private fun showErrorMessage(message: String) {
        binding.pbHome.isVisible = false
        binding.btnHomeErrorRetry.isVisible = true
        binding.tvHomeErrorMessage.isVisible = true
        binding.tvHomeErrorMessage.text = message
    }

    private fun setupRetryButton() {
        binding.btnHomeErrorRetry.setOnClickListener { viewModel.getRecipes() }
    }


    override fun onRecipeItemClick(uuid: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, DetailsFragment.newInstance(uuid))
            .addToBackStack(null)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        searchQuery = searchView.query
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_VIEW_QUERY_KEY, searchQuery)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}