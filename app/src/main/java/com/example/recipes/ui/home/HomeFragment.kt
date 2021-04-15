package com.example.recipes.ui.home

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.databinding.HomeFragmentBinding
import com.example.recipes.ui.details.DetailsFragment
import com.example.recipes.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), RecipeHomeAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: HomeViewModel
    private val recyclerAdapter = RecipeHomeAdapter(this)
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = HomeFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSearchTypeSpinner()
        setupSortTypeSpinner()
        setupRecycler()
        setupViewModel()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.recipesLD.observe(viewLifecycleOwner, { recyclerAdapter.submitList(it) })
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

        binding.spnSearchOver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var skipFirst = 0
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                if (view != null) skipFirst++
                if (skipFirst > 1) viewModel.setSearchSpinnerState(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
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
        binding.spnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var skipFirst = 0
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                if (view != null) skipFirst++
                if (skipFirst > 1) viewModel.setSortSpinnerState(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth = Int.MAX_VALUE

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRecipeItemClick(uuid: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, DetailsFragment.newInstance(uuid))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}