package com.example.recipes.di.fragments

import androidx.lifecycle.ViewModel
import com.example.recipes.di.viewmodels.ViewModelKey
import com.example.recipes.ui.details.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DetailsFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun detailsFragmentViewModel(viewModel: DetailsViewModel): ViewModel
}