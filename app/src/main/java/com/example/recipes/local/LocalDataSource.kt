package com.example.recipes.local

import com.example.recipes.repository.model.Recipe
import com.example.recipes.utils.datawrappers.Result

interface LocalDataSource {

    suspend fun saveRecipes(recipes: List<Recipe>)

    suspend fun getRecipes(): Result<List<Recipe>>

}