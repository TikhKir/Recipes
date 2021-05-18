package com.example.recipes

import android.app.Application
import com.example.recipes.di.DaggerAppComponent

class RecipeApp : Application() {

    val appComponent = DaggerAppComponent.builder()
        .appContext(this)
        .build()

}