package com.example.recipes.di

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
    fun provideRecipeRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.RECIPE_BASE_URL)
            .build()

    @Singleton
    @Provides
    fun provideRecipeService(retrofit: Retrofit): RecipeService =
        retrofit.create(RecipeService::class.java)

    @Singleton
    @Provides
    fun provideNetworkDataSource(recipeService: RecipeService): NetworkDataSource =
        NetworkDataSourceImpl(recipeService)

}