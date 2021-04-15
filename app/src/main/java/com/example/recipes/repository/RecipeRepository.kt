package com.example.recipes.repository

import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.Result

interface RecipeRepository {

    suspend fun getRecipes(): Result<List<Recipe>>

    suspend fun getRecipeByUUID(uuid: String): Result<Recipe>

}