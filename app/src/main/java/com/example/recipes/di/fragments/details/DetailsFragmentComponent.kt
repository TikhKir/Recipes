package com.example.recipes.di.fragments.details

import com.example.recipes.di.fragments.picture.PictureFragmentComponent
import com.example.recipes.di.viewmodels.ViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [DetailsFragmentModule::class])
@DetailsFragmentScope
interface DetailsFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailsFragmentComponent
    }

    fun viewModelFactory(): ViewModelFactory
    fun pictureFragmentComponent(): PictureFragmentComponent.Factory
}