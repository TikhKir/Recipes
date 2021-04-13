package com.example.recipes.network

import com.example.recipes.network.model.RawSimilarItem
import com.example.recipes.network.model.RecipeSingleResponse
import com.example.recipes.network.model.RecipesListResponse
import com.example.recipes.repository.model.Recipe
import com.example.recipes.repository.model.SimilarRecipe
import com.example.recipes.utils.BASE_RECIPE_URL
import com.example.recipes.utils.datawrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val recipeService: RecipeService
) : NetworkDataSource {


    override suspend fun getRecipeList(): Result<List<Recipe>> =
        executeApiCall { recipeService.getRecipes() }
            .transformInside(::rawResponseToRecipeList)

    override suspend fun getRecipeByUUID(uuid: String): Result<Recipe> =
        executeApiCall { recipeService.getRecipeByUUID(uuid) }
            .transformInside(::rawResponseSingleToRecipe)

    private suspend fun <T> executeApiCall(
        coroutine: suspend () -> T,
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(coroutine.invoke())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private fun rawResponseSingleToRecipe(response: RecipeSingleResponse): Recipe {
        with(response.rawRecipe) {
            return Recipe(
                uuid = uuid,
                name = name,
                description = description,
                instructions = instructions,
                lastUpdated = lastUpdated,
                images = images,
                similar = rawSimilarToSimilarList(rawSimilar),
                difficulty = difficulty
            )
        }
    }

    private fun rawResponseToRecipeList(response: RecipesListResponse): List<Recipe> {
        return response.rawRecipes.map { rawRecipe ->
            Recipe(
                uuid = rawRecipe.uuid,
                name = rawRecipe.name,
                description = rawRecipe.description,
                instructions = rawRecipe.instructions,
                lastUpdated = rawRecipe.lastUpdated,
                images = rawRecipe.images,
                similar = rawSimilarToSimilarList(rawRecipe.rawSimilar),
                difficulty = rawRecipe.difficulty
            )
        }
    }

    private fun rawSimilarToSimilarList(rawSimilarList: List<RawSimilarItem>): List<SimilarRecipe> {
        return rawSimilarList.map { rawSimilar ->
            SimilarRecipe(
                uuid = rawSimilar.uuid,
                name = rawSimilar.name,
                imageURL = rawSimilar.image
            )
        }
    }

}