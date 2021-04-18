package com.example.recipes.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemDetailSimilarBinding
import com.example.recipes.domain.model.SimilarRecipe
import com.example.recipes.utils.loadImage

class SimilarAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<SimilarRecipe, RecyclerView.ViewHolder>(RecipeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bindingView = ItemDetailSimilarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(bindingView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipe = getItem(position) as SimilarRecipe
        (holder as RecipeViewHolder).bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: ItemDetailSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(similar: SimilarRecipe) {
            binding.tvItemSimilar.text = similar.name
            binding.ivItemSimilar.loadImage(similar.imageURL)
            binding.root.setOnClickListener { itemClickListener.onSimilarItemClick(similar.uuid) }
        }
    }

    interface OnItemClickListener {
        fun onSimilarItemClick(uuid: String)
    }

    class RecipeDiffUtil : DiffUtil.ItemCallback<SimilarRecipe>() {
        override fun areItemsTheSame(oldItem: SimilarRecipe, newItem: SimilarRecipe): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: SimilarRecipe, newItem: SimilarRecipe): Boolean {
            return oldItem == newItem
        }
    }
}