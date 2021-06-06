package com.example.recipes.di.fragments.home

import com.example.recipes.di.fragments.details.DetailsFragmentComponent
import com.example.recipes.di.viewmodels.ViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [HomeFragmentModule::class])
@HomeFragmentScope
interface HomeFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeFragmentComponent
    }

    fun viewModelFactory(): ViewModelFactory
    fun detailsFragmentComponent(): DetailsFragmentComponent.Factory
}



