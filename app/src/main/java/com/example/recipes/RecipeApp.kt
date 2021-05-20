package com.example.recipes

import android.app.Application
import com.example.recipes.di.application.DaggerAppComponent


class RecipeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        DI.appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }
}