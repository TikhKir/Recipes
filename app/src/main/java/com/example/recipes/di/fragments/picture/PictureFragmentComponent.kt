package com.example.recipes.di.fragments.picture

import com.example.recipes.ui.picture.PictureFragment
import dagger.Subcomponent

@Subcomponent
@PictureFragmentScope
interface PictureFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PictureFragmentComponent
    }

    fun inject(fragment: PictureFragment)
}