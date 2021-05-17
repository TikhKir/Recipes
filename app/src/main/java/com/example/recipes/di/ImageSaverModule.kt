package com.example.recipes.di

import android.content.Context
import com.example.recipes.utils.ImageSaver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ImageSaverModule {

    @Provides
    @Singleton
    fun provideImageSaver(context: Context): ImageSaver = ImageSaver(context)

}