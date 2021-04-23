package com.example.recipes.di

import com.example.recipes.domain.SortAndSearchRecipesUseCase
import com.example.recipes.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SortAndSearchUseCaseModule {

    @Provides
    @Singleton
    fun provideSortAndSearchUseCase(repository: RecipeRepository) =
        SortAndSearchRecipesUseCase(repository)
}