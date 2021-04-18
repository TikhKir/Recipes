package com.example.recipes.di

import android.content.Context
import com.example.recipes.utils.ImageSaver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageSaverModule {

    @Provides
    @Singleton
    fun provideImageSaver(@ApplicationContext context: Context): ImageSaver =
        ImageSaver(context)

}