package com.example.recipes.repository

import com.example.recipes.domain.model.Recipe
import com.example.recipes.network.NetworkDataSource
import com.example.recipes.utils.Result
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource
) : RecipeRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return network.getRecipeList()
    }

    override suspend fun getRecipeByUUID(uuid: String): Result<Recipe> {
        return network.getRecipeByUUID(uuid)
    }


}