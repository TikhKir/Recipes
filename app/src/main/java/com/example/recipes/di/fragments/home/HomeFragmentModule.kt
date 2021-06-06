package com.example.recipes.di.fragments.home

import androidx.lifecycle.ViewModel
import com.example.recipes.di.viewmodels.ViewModelKey
import com.example.recipes.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun detailsFragmentViewModel(viewModel: HomeViewModel): ViewModel
}