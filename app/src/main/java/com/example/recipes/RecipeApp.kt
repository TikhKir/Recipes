package com.example.recipes

import android.app.Application
import com.example.recipes.di.DI
import com.example.recipes.di.application.AppComponent
import com.example.recipes.di.application.DaggerAppComponent
import com.example.recipes.di.common.HasAppComponent


class RecipeApp : Application(), HasAppComponent {

    override val component: AppComponent get() = DI.appComponent

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