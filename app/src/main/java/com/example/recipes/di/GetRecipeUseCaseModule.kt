package com.example.recipes.di

import com.example.recipes.domain.GetRecipesUseCase
import com.example.recipes.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetRecipeUseCaseModule {

    @Singleton
    @Provides
    fun provideRecipeUseCase(repository: RecipeRepository): GetRecipesUseCase =
        GetRecipesUseCase(repository)

}