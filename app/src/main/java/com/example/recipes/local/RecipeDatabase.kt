package com.example.recipes.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipes.local.converters.ImageConverter
import com.example.recipes.local.converters.SimilarRecipeConverter
import com.example.recipes.local.model.RecipeDB

@Database(entities = [RecipeDB::class], version = 1, exportSchema = false)
@TypeConverters(ImageConverter::class, SimilarRecipeConverter::class)
abstract class RecipeDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "recipe.db"
    }

    abstract fun recipeDao(): RecipeDao

}