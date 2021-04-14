package com.example.recipes.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipes.local.converters.ImageConverter
import com.example.recipes.local.converters.SimilarRecipeConverter
import com.example.recipes.domain.model.SimilarRecipe

@Entity(
    tableName = "recipes",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class RecipeDB(
    @PrimaryKey(autoGenerate = true)
    val localRecipeId: Int? = null,
    val uuid: String,
    val name: String,
    val description: String,
    val instructions: String,
    val lastUpdated: Int,
    @TypeConverters(ImageConverter::class)
    val images: List<String>,
    @TypeConverters(SimilarRecipeConverter::class)
    val similar: List<SimilarRecipe>,
    val difficulty: Int,
)
