package com.example.recipes.repository

import com.example.recipes.domain.model.Recipe
import com.example.recipes.network.NetworkDataSource
import com.example.recipes.utils.Result
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource
) : RecipeRepository {

    private var cachedRecipesResult: Result<List<Recipe>> = Result.Success(listOf())

    override suspend fun getRecipes(forcedUpdate: Boolean): Result<List<Recipe>> {
        if (cachedRecipesResult.resultIsEmpty() || forcedUpdate) {
            cachedRecipesResult = network.getRecipeList()
        }
        return cachedRecipesResult
    }



    override suspend fun getRecipeByUUID(uuid: String): Result<Recipe> {
        return network.getRecipeByUUID(uuid)
    }


}