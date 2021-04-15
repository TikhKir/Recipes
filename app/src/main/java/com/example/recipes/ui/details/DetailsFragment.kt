package com.example.recipes.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipes.R
import com.example.recipes.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(uuid: String): DetailsFragment {
            val args = Bundle().apply { putString(EXTRA_UUID, uuid) }
            return DetailsFragment().apply { arguments = args }
        }

        private const val EXTRA_UUID = "EXTRA_UUID"
    }

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var uuidArg: String
    private lateinit var viewModel: DetailsViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initArgs()
        _binding = DetailsFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewModel()
    }



    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel.getRecipe(uuidArg)
    }

    private fun initArgs() {
        uuidArg = arguments?.getString(EXTRA_UUID)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}