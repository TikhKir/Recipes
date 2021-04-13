package com.example.recipes.local

import androidx.room.*
import com.example.recipes.local.model.RecipeDB
import com.example.recipes.local.model.RecipeWithSimilar
import com.example.recipes.local.model.SimilarRecipeDB

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewRecipes(recipes: List<RecipeDB>)

    @Insert(entity = SimilarRecipeDB::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewSimilarRecipes(similarRecipes: List<SimilarRecipeDB>)

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeDB>

    @Query("SELECT * FROM recipes WHERE uuid = :uuid")
    suspend fun getRecipeByUUID(uuid: String): RecipeDB

    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getRecipesWithSimilar(): List<RecipeWithSimilar>

    @Transaction
    @Query("SELECT * FROM recipes WHERE uuid = :uuid")
    suspend fun getRecipeWithSimilar(uuid: String): RecipeWithSimilar

}