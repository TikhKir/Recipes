package com.example.recipes.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipeWithSimilar(
    @Embedded
    val recipe: RecipeDB,
    @Relation(parentColumn = "localRecipeId", entityColumn = "parentUUID")
    val similar: List<SimilarRecipeDB>
)
