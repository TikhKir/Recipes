package com.example.recipes.di.common

import androidx.fragment.app.Fragment
import com.example.recipes.di.application.AppComponent
import com.example.recipes.di.fragments.details.DetailsFragmentComponent
import com.example.recipes.di.fragments.home.HomeFragmentComponent

interface HasAppComponent{
    val component: AppComponent
}

interface HasHomeFragmentComponent {
    val component: HomeFragmentComponent
}

interface HasDetailsFragmentComponent {
    val component: DetailsFragmentComponent
}


object AppComponentFinder {
    fun find(fragment: Fragment): AppComponent =
        ComponentFinder.find<HasAppComponent>(fragment).component
}

object HomeFragmentComponentFinder {
    fun find(fragment: Fragment): HomeFragmentComponent =
        ComponentFinder.find<HasHomeFragmentComponent>(fragment).component
}

object DetailsFragmentComponentFinder {
    fun find(fragment: Fragment): DetailsFragmentComponent =
        ComponentFinder.find<HasDetailsFragmentComponent>(fragment).component
}