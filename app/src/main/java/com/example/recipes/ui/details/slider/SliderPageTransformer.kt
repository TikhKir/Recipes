package com.example.recipes.ui.details.slider

import android.view.View
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.example.recipes.R
import kotlin.math.abs

class SliderPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        val counter = page.findViewById<CardView>(R.id.cv_image_counter)

        counter.translationX = -position * 1.0F * page.width
        val r = 1 - abs(position)
        page.scaleY = 0.85F + r * 0.15F
    }
}