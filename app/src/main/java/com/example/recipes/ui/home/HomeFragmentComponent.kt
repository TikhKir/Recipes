package com.example.recipes.ui.home

import androidx.lifecycle.ViewModel
import com.example.recipes.di.NetworkDataSourceModule
import com.example.recipes.di.RecipeRepositoryModule
import com.example.recipes.di.viewmodels.ViewModelFactory
import com.example.recipes.di.viewmodels.ViewModelKey
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Component(modules = [HomeFragmentModule::class, RecipeRepositoryModule::class, NetworkDataSourceModule::class])
@Singleton
interface HomeFragmentComponent {

    fun viewModelFactory(): ViewModelFactory
    fun build() : HomeFragmentComponent

    companion object {
        fun create() = DaggerHomeFragmentComponent.create().build()
    }

}

@Module
interface HomeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeFragmentViewModel(viewModel: HomeViewModel): ViewModel
}