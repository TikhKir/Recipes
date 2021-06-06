package com.example.recipes.di.application

import android.content.Context
import com.example.recipes.di.fragments.home.HomeFragmentComponent
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

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context) : AppComponent
    }

    fun homeFragmentComponent(): HomeFragmentComponent.Factory


}