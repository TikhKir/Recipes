package com.example.recipes.ui.details.slider

import android.view.View
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.example.recipes.R
import kotlin.math.abs

class SliderPageTransformer : ViewPager2.PageTransformer {
    companion object {
        private const val zoomValue = 0.15F
    }

    override fun transformPage(page: View, position: Float) {

        val counter = page.findViewById<CardView>(R.id.cv_image_counter)
        counter.translationX = -position * page.width

        val positionOffset = 1F - abs(position)
        page.scaleY = (1F - zoomValue) + positionOffset * zoomValue
        page.scaleX = (1F - zoomValue) + positionOffset * zoomValue
    }
}