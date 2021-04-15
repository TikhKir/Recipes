package com.example.recipes.local

import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.Result

interface LocalDataSource {

    suspend fun saveRecipes(recipes: List<Recipe>)

    suspend fun getRecipes(): Result<List<Recipe>>

}