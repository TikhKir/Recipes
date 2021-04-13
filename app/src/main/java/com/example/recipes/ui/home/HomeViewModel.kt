package com.example.recipes.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.repository.RecipeRepository
import com.example.recipes.repository.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: RecipeRepository
) : ViewModel() {

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }

    private val recipesMutable = MutableLiveData<List<Recipe>>()
    val recipesLiveData get() = recipesMutable


    init {
        getRecipes()
        //getRecipeByUUID("fc988768-c1e9-11e6-a4a6-cec0c932ce01")
    }


    private fun getRecipes() = viewModelScope.launch(Dispatchers.IO) {
        repo.getRecipes()
            .onSuccess {
                it.map { Log.e(TAG, it.name) }
            }.onError {
                Log.e(TAG, it.message.toString())
            }
    }






}