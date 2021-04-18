package com.example.recipes.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun SearchView.searchWatcherFlow(): Flow<String> = callbackFlow {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            offer(newText)
            return false
        }
    }
    setOnQueryTextListener(listener)
    awaitClose()
}

fun Spinner.setFakeSelectSkipWatcher(execute: (position: Int) -> Unit, defPosition: Int = 0) {
    val listener = object : AdapterView.OnItemSelectedListener {
        var previousIsNull = -1
        var notSkip = false
        override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
            if (notSkip) execute(position)
            else {
                if ((view != null && position == defPosition) ||
                    (view == null && position == defPosition) ||
                    (view != null && previousIsNull == 1 && position != defPosition)
                ) notSkip = true
            }
            previousIsNull = if (view == null) 1 else 0
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
    onItemSelectedListener = listener
}

fun ImageView.loadImage(imageUri: String?) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 3f
    circularProgressDrawable.centerRadius = 15f
    circularProgressDrawable.start()

    Glide.with(context)
        .load(imageUri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(circularProgressDrawable)
        .into(this)
}

