package com.example.recipes.network

import com.example.recipes.network.model.RecipeSingleResponse
import com.example.recipes.network.model.RecipesListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {

    @GET("recipes")
    suspend fun getRecipes(): RecipesListResponse

    @GET("recipes/{uuid}")
    suspend fun getRecipeByUUID(
        @Path("uuid") uuid: String
    ): RecipeSingleResponse
}