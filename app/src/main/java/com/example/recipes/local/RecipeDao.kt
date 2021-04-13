package com.example.recipes.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipes.local.model.RecipeDB

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewRecipes(recipes: List<RecipeDB>)

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeDB>

    @Query("SELECT * FROM recipes WHERE uuid = :uuid")
    suspend fun getRecipeByUUID(uuid: String): RecipeDB
}