package com.example.recipes.ui.details.slider


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recipes.databinding.ItemViewPagerBinding
import com.example.recipes.utils.loadImage

class SliderAdapter(
    private val imageClickListener: OnImageClickListener
) : ListAdapter<String, RecyclerView.ViewHolder>(StringDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bindingView = ItemViewPagerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        if (itemCount <= 1) bindingView.cvImageCounter.isVisible = false
        return StringViewHolder(bindingView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageUrl = getItem(position) as String
        (holder as StringViewHolder).bind(imageUrl, position)
    }


    inner class StringViewHolder(private val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String, position: Int) {
            binding.ivPagerImage.loadImage(imageUrl)
            val countStr = "${position+1}/$itemCount"
            binding.tvImageCounter.text = countStr
            binding.root.setOnClickListener { imageClickListener.onImageClick(imageUrl) }
        }
    }


    class StringDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return true
        }
    }

    interface OnImageClickListener {
        fun onImageClick(imageUrl: String)
    }

}