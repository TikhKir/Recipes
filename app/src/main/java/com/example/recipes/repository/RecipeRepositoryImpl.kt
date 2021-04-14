package com.example.recipes.repository

import com.example.recipes.local.LocalDataSource
import com.example.recipes.network.NetworkDataSource
import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.datatype.Result
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource,
    private val local: LocalDataSource
) : RecipeRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        network.getRecipeList()
            .onSuccess { local.saveRecipes(it) }
        return local.getRecipes()
    }

    override suspend fun getRecipeByUUID(uuid: String): Result<Recipe> {
        return network.getRecipeByUUID(uuid)
    }


}