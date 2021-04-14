package com.example.recipes.domain.model

data class Recipe(
    val uuid: String,
    val name: String,
    val description: String,
    val instructions: String,
    val lastUpdated: Int,
    val images: List<String>,
    val similar: List<SimilarRecipe>,
    val difficulty: Int,
)
