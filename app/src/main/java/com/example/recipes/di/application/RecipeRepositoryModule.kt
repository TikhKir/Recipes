package com.example.recipes.di.application

import com.example.recipes.network.NetworkDataSource
import com.example.recipes.repository.RecipeRepository
import com.example.recipes.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RecipeRepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(networkDataSource: NetworkDataSource, ): RecipeRepository =
        RecipeRepositoryImpl(networkDataSource)

}