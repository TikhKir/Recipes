package com.example.recipes.local.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.recipes.local.converters.ImageConverter
import com.example.recipes.repository.model.SimilarRecipe

@Keep
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
    val difficulty: Int,
)
