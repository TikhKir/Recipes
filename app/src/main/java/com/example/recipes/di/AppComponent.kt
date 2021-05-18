package com.example.recipes.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ImageSaverModule::class,
        NetworkDataSourceModule::class,
        RecipeRepositoryModule::class,
        SortAndSearchUseCaseModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder
        fun build(): AppComponent
    }
}