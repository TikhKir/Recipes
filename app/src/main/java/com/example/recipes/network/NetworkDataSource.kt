package com.example.recipes.network

import com.example.recipes.repository.model.Recipe
import com.example.recipes.utils.datawrappers.Result

interface NetworkDataSource {

    suspend fun getRecipeList(): Result<List<Recipe>>

    suspend fun getRecipeByUUID(uuid: String): Result<Recipe>

}