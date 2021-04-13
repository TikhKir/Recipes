package com.example.recipes.local

import com.example.recipes.local.model.RecipeDB
import com.example.recipes.repository.model.Recipe
import com.example.recipes.utils.datawrappers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : LocalDataSource {


    override suspend fun saveRecipes(recipes: List<Recipe>) {
        val transformedRecipes = recipesToRecipesDB(recipes)
        recipeDao.saveNewRecipes(transformedRecipes)
    }

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return safeExecuteAndWrap { recipeDao.getAllRecipes() }
            .transformInside(::recipesDBtoRecipes)
    }

    private suspend fun <T> safeExecuteAndWrap(
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

    private fun recipesToRecipesDB(recipes: List<Recipe>): List<RecipeDB> =
        recipes.map { recipeToRecipeDB(it) }

    private fun recipeToRecipeDB(recipe: Recipe): RecipeDB =
        RecipeDB(
            uuid = recipe.uuid,
            name = recipe.name,
            description = recipe.description,
            instructions = recipe.instructions,
            lastUpdated = recipe.lastUpdated,
            images = recipe.images,
            difficulty = recipe.difficulty,
            similar = recipe.similar
        )

    private fun recipesDBtoRecipes(recipesDB: List<RecipeDB>): List<Recipe> =
        recipesDB.map { recipeDBtoRecipe(it) }

    private fun recipeDBtoRecipe(recipeDB: RecipeDB): Recipe =
        Recipe(
            uuid = recipeDB.uuid,
            name = recipeDB.name,
            description = recipeDB.description,
            instructions = recipeDB.instructions,
            lastUpdated = recipeDB.lastUpdated,
            images = recipeDB.images,
            similar = recipeDB.similar,
            difficulty = recipeDB.difficulty
        )

}