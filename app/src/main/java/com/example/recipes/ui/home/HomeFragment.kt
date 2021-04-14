package com.example.recipes.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.recipes.R
import com.example.recipes.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSearchTypeSpinner()
        setupSortTypeSpinner()
        setupViewModel()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun setupRecycler() {
        
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}