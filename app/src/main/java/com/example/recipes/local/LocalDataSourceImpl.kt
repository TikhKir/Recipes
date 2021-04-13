package com.example.recipes.local

import com.example.recipes.local.model.RecipeDB
import com.example.recipes.local.model.RecipeWithSimilar
import com.example.recipes.local.model.SimilarRecipeDB
import com.example.recipes.repository.model.Recipe
import com.example.recipes.repository.model.SimilarRecipe
import com.example.recipes.utils.datawrappers.Result
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : LocalDataSource {


    override suspend fun saveRecipes(recipes: List<Recipe>) {
        val transformedRecipes = recipeListToRecipeListDB(recipes)
        recipeDao.saveNewRecipes(transformedRecipes)

        recipes.forEach { recipe ->
            val transformedSimilar = similarListToSimilarListDB(recipe.uuid, recipe.similar)
            if (transformedSimilar.isNotEmpty()) recipeDao.saveNewSimilarRecipes(transformedSimilar)
        }
    }

    override suspend fun getRecipes(): Result<List<Recipe>> {
        TODO("Not yet implemented")
    }








    private fun recipeListToRecipeListDB(recipes: List<Recipe>): List<RecipeDB> =
        recipes.map { recipeToRecipeDB(it) }

    private fun similarListToSimilarListDB(parentId: String, similar: List<SimilarRecipe>)
            : List<SimilarRecipeDB> = similar.map { similarToSimilarDB(parentId, it) }

    private fun similarToSimilarDB(parentId: String, similar: SimilarRecipe): SimilarRecipeDB {
        return SimilarRecipeDB(
            uuid = similar.uuid,
            name = similar.name,
            imageURL = similar.imageURL,
            parentUUID = parentId
        )
    }

    private fun recipeToRecipeDB(recipe: Recipe): RecipeDB {
        return RecipeDB(
            uuid = recipe.uuid,
            name = recipe.name,
            description = recipe.description,
            instructions = recipe.instructions,
            lastUpdated = recipe.lastUpdated,
            images = recipe.images,
            difficulty = recipe.difficulty
        )
    }

}