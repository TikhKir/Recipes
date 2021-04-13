package com.example.recipes.di

import android.content.Context
import androidx.room.Room
import com.example.recipes.local.LocalDataSource
import com.example.recipes.local.LocalDataSourceImpl
import com.example.recipes.local.RecipeDao
import com.example.recipes.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipeDatabase =
        Room.databaseBuilder(context, RecipeDatabase::class.java, RecipeDatabase.DB_NAME)
            .build()

    @Singleton
    @Provides
    fun provideDao(database: RecipeDatabase): RecipeDao =
        database.recipeDao()

    @Singleton
    @Provides
    fun provideLocationDataSource(recipeDao: RecipeDao): LocalDataSource =
        LocalDataSourceImpl(recipeDao)
}