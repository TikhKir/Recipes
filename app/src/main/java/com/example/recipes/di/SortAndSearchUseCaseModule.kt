package com.example.recipes.di

import com.example.recipes.domain.SortAndSearchRecipesUseCase
import com.example.recipes.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SortAndSearchUseCaseModule {

    @Provides
    @Singleton
    fun provideSortAndSearchUseCase(repository: RecipeRepository) =
        SortAndSearchRecipesUseCase(repository)
}