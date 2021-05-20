package com.example.recipes.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.example.recipes.DI
import com.example.recipes.R
import com.example.recipes.databinding.DetailsFragmentBinding
import com.example.recipes.domain.model.Recipe
import com.example.recipes.domain.model.SimilarRecipe
import com.example.recipes.ui.details.recycler.SimilarAdapter
import com.example.recipes.ui.details.recycler.SimilarMarginDecorator
import com.example.recipes.ui.details.slider.SliderAdapter
import com.example.recipes.ui.details.slider.SliderPageTransformer
import com.example.recipes.ui.picture.PictureFragment
import com.example.recipes.utils.State
import com.example.recipes.utils.convertHtml


class DetailsFragment : Fragment(), SliderAdapter.OnImageClickListener,
    SimilarAdapter.OnItemClickListener {

    companion object {
        private const val LOOP_FRAGMENT_TAG = "LOOP_DETAILS_FRAGMENT_TAG"
        private const val EXTRA_UUID = "EXTRA_UUID"
        fun newInstance(uuid: String): DetailsFragment {
            val args = Bundle().apply { putString(EXTRA_UUID, uuid) }
            return DetailsFragment().apply { arguments = args }
        }
    }

    private val component by lazy { DI.appComponent.plusFragmentComponent().create() }
    private val viewModel by viewModels<DetailsViewModel> { component.viewModelFactory() }

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var uuidArg: String
    private val sliderAdapter = SliderAdapter(this)
    private val similarAdapter = SimilarAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupImageSlider()
        setupRetryButton()
        setupRecycler()
    }

    private fun setupRecycler() {
        binding.rvDetailsSimilar.apply {
            addItemDecoration(SimilarMarginDecorator(resources.getDimensionPixelSize(R.dimen.margin_normal)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.stateLD.observe(viewLifecycleOwner, {
            if (it is State.Default) viewModel.getRecipe(uuidArg)
            updateLoadingState(it)
        })
        viewModel.detailedRecipeLD.observe(viewLifecycleOwner, {
            sliderAdapter.submitList(it.images)
            updateSimilar(it.similar)
            updateRecipeInfo(it)
        })
    }


    private fun updateSimilar(similar: List<SimilarRecipe>) {
        if (similar.isNotEmpty()) {
            similarAdapter.submitList(similar)
            binding.rvDetailsSimilar.isVisible = true
            binding.tvDetailsSimilarLabel.isVisible = true
        }
    }

    private fun updateRecipeInfo(recipe: Recipe) = with(binding) {
        tvDetailRecipeTitle.text = recipe.name

        tvDetailRecipeDescription.isVisible = recipe.description.isNotEmpty()
        tvDetailRecipeDescription.text = recipe.description

        val instruction = convertHtml(recipe.instructions)
        tvDetailRecipeInstructions.isVisible = instruction.isNotEmpty()
        tvDetailInstructionsLabel.isVisible = instruction.isNotEmpty()
        tvDetailRecipeInstructions.text = instruction

        rbDifficulty.rating = recipe.difficulty.toFloat()

        when (recipe.difficulty) {
            1 -> tvDifficultyLabel.text = getString(R.string.very_easy)
            2 -> tvDifficultyLabel.text = getString(R.string.easy)
            3 -> tvDifficultyLabel.text = getString(R.string.normal)
            4 -> tvDifficultyLabel.text = getString(R.string.difficult)
            5 -> tvDifficultyLabel.text = getString(R.string.very_difficult)
            else -> difficultyContainer.isVisible = false
        }
    }

    private fun setupImageSlider() {
        binding.vpImageSlide.apply {
            adapter = sliderAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(
                CompositePageTransformer().apply {
                    addTransformer(SliderPageTransformer())
                }
            )
        }
    }

    private fun updateLoadingState(state: State) = when (state) {
        is State.Default -> setLoading(false)
        is State.Loading -> setLoading(true)
        is State.Error -> showErrorMessage(state.errorMessage)
        is State.Success -> setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) = with(binding) {
        svDetailContent.isVisible = !isLoading
        pbDetails.isVisible = isLoading
        tvDetailsErrorMessage.isVisible = false
        btnDetailsErrorRetry.isVisible = false
    }

    private fun showErrorMessage(message: String) = with(binding) {
        svDetailContent.isVisible = false
        pbDetails.isVisible = false
        btnDetailsErrorRetry.isVisible = true
        tvDetailsErrorMessage.isVisible = true
        tvDetailsErrorMessage.text = message
    }

    private fun setupRetryButton() {
        binding.btnDetailsErrorRetry.setOnClickListener { viewModel.getRecipe(uuidArg) }
    }

    private fun initArgs() {
        uuidArg = arguments?.getString(EXTRA_UUID)!!
    }

    override fun onImageClick(imageUrl: String) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, PictureFragment.newInstance(imageUrl))
            .addToBackStack(null)
            .commit()
    }

    override fun onSimilarItemClick(uuid: String) {
        /*хотя апи не предоставляет рецепты, которые имеют перекрестные ссылки друг на друга,
        я добавил защиту от такого случая, чтобы не заполнять стек фрагментами
        похожих рецептов, если пользователь будет переходить по ним многократно*/

        val loopFragment = parentFragmentManager.findFragmentByTag(LOOP_FRAGMENT_TAG)
        if (loopFragment != null) parentFragmentManager.popBackStack()

        parentFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, DetailsFragment.newInstance(uuid), LOOP_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}