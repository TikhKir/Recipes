package com.example.recipes.ui.details.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SimilarMarginDecorator(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            right = spaceSize
            if (parent.getChildAdapterPosition(view) == 0) left = spaceSize
        }
    }
}