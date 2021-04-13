package com.example.recipes.di

import com.example.recipes.network.NetworkDataSource
import com.example.recipes.network.NetworkDataSourceImpl
import com.example.recipes.network.RecipeService
import com.example.recipes.utils.BASE_RECIPE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideRecipeRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_RECIPE_URL)
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