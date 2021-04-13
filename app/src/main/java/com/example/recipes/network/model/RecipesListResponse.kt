package com.example.recipes.network.model

import com.google.gson.annotations.SerializedName

data class RecipesListResponse(
    @SerializedName("recipes")
    val rawRecipes: List<RawRecipe>
)
