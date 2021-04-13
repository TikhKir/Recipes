package com.example.recipes.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.local.LocalDataSource
import com.example.recipes.network.NetworkDataSource
import com.example.recipes.network.NetworkDataSourceImpl
import com.example.recipes.repository.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val network: NetworkDataSource,
    private val local: LocalDataSource
) : ViewModel() {



    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }

    init {
        getRecipes()
        //getRecipeByUUID("fc988768-c1e9-11e6-a4a6-cec0c932ce01")
    }


    private fun getRecipes() = viewModelScope.launch(Dispatchers.IO) {
        network.getRecipeList()
            .onSuccess {
                it.map { Log.e(TAG, it.name ) }
                saveRecipes(it)
            }.onError {
                Log.e(TAG, it.message.toString() )
            }
    }

    private fun getRecipeByUUID(uuid: String) = viewModelScope.launch(Dispatchers.IO) {
        network.getRecipeByUUID(uuid)
            .onSuccess { Log.e(TAG, it.name ) }
            .onError { Log.e(TAG, it.message.toString() ) }
    }

    private fun saveRecipes(recipes: List<Recipe>) = viewModelScope.launch(Dispatchers.IO) {
        local.saveRecipes(recipes)
    }


}