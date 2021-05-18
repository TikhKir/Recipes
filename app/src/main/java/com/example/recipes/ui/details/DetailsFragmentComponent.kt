package com.example.recipes.ui.details

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


@Component(modules = [DetailsFragmentModule::class, RecipeRepositoryModule::class, NetworkDataSourceModule::class])
@Singleton
interface DetailsFragmentComponent {

    fun viewModelFactory(): ViewModelFactory
    fun build() : DetailsFragmentComponent

    companion object {
        fun create() = DaggerDetailsFragmentComponent.create().build()
    }

}

@Module
interface DetailsFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun detailsFragmentViewModel(viewModel: DetailsViewModel): ViewModel
}