package com.example.recipes.di.fragments

import com.example.recipes.di.viewmodels.ViewModelFactory
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DetailsFragmentModule::class,
        HomeFragmentModule::class
    ]
)
@FragmentScope
interface FragmentSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentSubComponent
    }

    fun viewModelFactory(): ViewModelFactory
}



