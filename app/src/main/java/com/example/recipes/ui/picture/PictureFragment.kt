package com.example.recipes.ui.picture

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.recipes.MainActivity
import com.example.recipes.R
import com.example.recipes.databinding.PictureFragmentBinding
import com.example.recipes.utils.ImageSaver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PictureFragment : Fragment() {

    companion object {
        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        private const val VISIBILITY_KEY = "VISIBILITY_KEY"
        fun newInstance(imageUrl: String): PictureFragment {
            val args = Bundle().apply { putString(EXTRA_IMAGE_URL, imageUrl) }
            return PictureFragment().apply { arguments = args }
        }
    }

    @Inject
    lateinit var imageSaver: ImageSaver
    private var bitmapToSave: Bitmap? = null

    private var _binding: PictureFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUrl: String
    private var visibility = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PictureFragmentBinding
            .inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visibility = savedInstanceState?.getBoolean(VISIBILITY_KEY) ?: false
        (activity as MainActivity).fullScreenModeOn()
        initArgs()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisible(visibility)
        setupImage()
        setupSaveButton()
        setupViewVisibilityChanger()
    }

    private fun setVisible(isVisible: Boolean) {
        binding.btSave.isVisible = isVisible
    }

    private fun setupViewVisibilityChanger() {
        binding.flHidden.setOnClickListener {
            if (visibility) {
                binding.btSave.isVisible = false
                visibility = false
            } else {
                binding.btSave.isVisible = true
                visibility = true
            }
        }
    }

    private fun initArgs() {
        imageUrl = arguments?.getString(EXTRA_IMAGE_URL)!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(VISIBILITY_KEY, visibility)
    }

    private fun setupSaveButton() {
        binding.btSave.setOnClickListener {
            if (bitmapToSave != null) imageSaver.save(bitmapToSave!!)
            else Toast.makeText(context, getString(R.string.image_is_not_loaded), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupImage() {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.ivBigPicture.setImageBitmap(resource)
                    bitmapToSave = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }


    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).fullScreenModeOff()
    }
}