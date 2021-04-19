package com.example.recipes.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemHomeRecipeBinding
import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.loadImage

class RecipeHomeAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<Recipe, RecyclerView.ViewHolder>(RecipeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bindingView = ItemHomeRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(bindingView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipe = getItem(position) as Recipe
        (holder as RecipeViewHolder).bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: ItemHomeRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) = with(binding) {
            tvRecipeTitle.text = recipe.name
            tvRecipeDescription.text = recipe.description
            ivRecipeItemImage.loadImage(recipe.images.firstOrNull())
            root.setOnClickListener { itemClickListener.onRecipeItemClick(recipe.uuid) }
        }
    }

    interface OnItemClickListener {
        fun onRecipeItemClick(uuid: String)
    }

    class RecipeDiffUtil : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}



