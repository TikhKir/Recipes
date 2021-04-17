package com.example.recipes.ui.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recipes.MainActivity
import com.example.recipes.R

class PictureFragment : Fragment() {

    companion object {
        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        fun newInstance(imageUrl: String): PictureFragment {
            val args = Bundle().apply { putString(EXTRA_IMAGE_URL, imageUrl) }
            return PictureFragment().apply { arguments = args }
        }
    }

    private lateinit var ivPicture: ImageView
    private lateinit var imageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.picture_fragment, container, false)
        ivPicture = view.findViewById(R.id.iv_big_picture)
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideSystemUI()
        initArgs()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(ivPicture)
    }

    private fun initArgs() {
        imageUrl = arguments?.getString(EXTRA_IMAGE_URL)!!
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showSystemUI()
    }

}