package com.example.recipes.network.model

import com.google.gson.annotations.SerializedName

data class RecipeSingleResponse(
    @SerializedName("recipe")
    val rawRecipe: RawRecipe
)
