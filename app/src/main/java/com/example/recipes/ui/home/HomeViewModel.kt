package com.example.recipes.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.domain.GetRecipesUseCase
import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.filterparameters.SearchType
import com.example.recipes.utils.filterparameters.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }



    private val searchType = MutableLiveData<SearchType>(SearchType.ByName)
    private val sortType = MutableLiveData<SortType>(SortType.Missing)

    private val recipesMutable = MutableLiveData<List<Recipe>>()
    val recipesLiveData get() = recipesMutable


    init {
        getRecipes()
        //getRecipeByUUID("fc988768-c1e9-11e6-a4a6-cec0c932ce01")
    }


    private fun getRecipes() = viewModelScope.launch(Dispatchers.IO) {
        getRecipesUseCase.execute("goat", SearchType.ByName, SortType.Missing)
            .onSuccess {
                it.map { Log.e(TAG, it.name) }
            }.onError {
                Log.e(TAG, it.message.toString())
            }
    }






}