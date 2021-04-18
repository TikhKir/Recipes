package com.example.recipes.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.recipes.R
import com.example.recipes.databinding.DetailsFragmentBinding
import com.example.recipes.domain.model.Recipe
import com.example.recipes.ui.details.slider.SliderAdapter
import com.example.recipes.ui.details.slider.SliderPageTransformer
import com.example.recipes.ui.picture.PictureFragment
import com.example.recipes.utils.State
import com.example.recipes.utils.convertHtml
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(), SliderAdapter.OnImageClickListener {

    companion object {
        private const val EXTRA_UUID = "EXTRA_UUID"
        fun newInstance(uuid: String): DetailsFragment {
            val args = Bundle().apply { putString(EXTRA_UUID, uuid) }
            return DetailsFragment().apply { arguments = args }
        }
    }

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var uuidArg: String
    private lateinit var viewModel: DetailsViewModel
    private val sliderAdapter = SliderAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupImageSlider()
        setupRetryButton()
    }


    private fun observeViewModel() {
        viewModel.stateLD.observe(viewLifecycleOwner, {
            if (it is State.Default) viewModel.getRecipe(uuidArg)
            updateLoadingState(it)
        })
        viewModel.detailedRecipeLD.observe(viewLifecycleOwner, {
            sliderAdapter.submitList(it.images)
            updateRecipeInfo(it)
        }
        )
    }


    private fun updateRecipeInfo(recipe: Recipe) {
        binding.tvDetailRecipeTitle.text = recipe.name

        if (recipe.description.isEmpty()) {
            binding.tvDetailRecipeDescription.isVisible = false
        } else {
            binding.tvDetailRecipeDescription.text = recipe.description
            binding.tvDetailRecipeDescription.isVisible = true
        }

        if (convertHtml(recipe.instructions).isEmpty()) {
            binding.tvDetailRecipeInstructions.isVisible = false
            binding.tvDetailInstructionsLabel.isVisible = false
        } else {
            binding.tvDetailRecipeInstructions.text = convertHtml(recipe.instructions)
            binding.tvDetailRecipeInstructions.isVisible = true
            binding.tvDetailInstructionsLabel.isVisible = true
        }

    }

    private fun setupImageSlider() {
        binding.vpImageSlide.adapter = sliderAdapter
        binding.vpImageSlide.clipToPadding = false
        binding.vpImageSlide.clipChildren = false
        binding.vpImageSlide.offscreenPageLimit = 3
        binding.vpImageSlide.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.vpImageSlide.setPageTransformer(
            CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer(SliderPageTransformer())
            }
        )
    }

    private fun updateLoadingState(state: State) = when (state) {
        is State.Default -> setLoading(false)
        is State.Loading -> setLoading(true)
        is State.Error -> showErrorMessage(state.errorMessage)
        is State.Success -> setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.svDetailContent.isVisible = !isLoading
        binding.pbDetails.isVisible = isLoading
        binding.tvDetailsErrorMessage.isVisible = false
        binding.btnDetailsErrorRetry.isVisible = false
    }

    private fun showErrorMessage(message: String) {
        binding.svDetailContent.isVisible = false
        binding.pbDetails.isVisible = false
        binding.btnDetailsErrorRetry.isVisible = true
        binding.tvDetailsErrorMessage.isVisible = true
        binding.tvDetailsErrorMessage.text = message
    }

    private fun setupRetryButton() {
        binding.btnDetailsErrorRetry.setOnClickListener { viewModel.getRecipe(uuidArg) }
    }

    private fun initArgs() {
        uuidArg = arguments?.getString(EXTRA_UUID)!!
    }

    override fun onImageClick(imageUrl: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, PictureFragment.newInstance(imageUrl))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}