package com.example.recipes.di.application

import com.example.recipes.BuildConfig
import com.example.recipes.network.NetworkDataSource
import com.example.recipes.network.NetworkDataSourceImpl
import com.example.recipes.network.RecipeService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideRecipeService(): RecipeService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.RECIPE_BASE_URL)
            .build()
            .create(RecipeService::class.java)

    @Singleton
    @Provides
    fun provideNetworkDataSource(recipeService: RecipeService): NetworkDataSource =
        NetworkDataSourceImpl(recipeService)

}