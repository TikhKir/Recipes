package com.example.recipes.di

import com.example.recipes.network.NetworkDataSource
import com.example.recipes.repository.RecipeRepository
import com.example.recipes.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeRepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(networkDataSource: NetworkDataSource, ): RecipeRepository =
        RecipeRepositoryImpl(networkDataSource)

}