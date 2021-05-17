package com.example.recipes.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.domain.SortAndSearchRecipesUseCase
import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.SEARCH_MIN_QUERY
import com.example.recipes.utils.State
import com.example.recipes.utils.filterparameters.SearchType
import com.example.recipes.utils.filterparameters.SortType
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val sortAndSearchUseCase: SortAndSearchRecipesUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }


    private var searchType: SearchType = SearchType.ByName
    private var sortType: SortType = SortType.Unsorted
    private var searchQuery: String? = null

    private val recipesMutable = MutableLiveData<List<Recipe>>()
    val recipesLD: LiveData<List<Recipe>> get() = recipesMutable

    private val state = MutableLiveData<State>(State.Default())
    val stateLD: LiveData<State> get() = state

    init {
        getRecipes()
    }


    fun getRecipes(forcedUpdate: Boolean = false) = viewModelScope.launch {
        state.postValue(State.Loading())
        sortAndSearchUseCase.execute(searchQuery, searchType, sortType, forcedUpdate)
            .onSuccess {
                recipesMutable.postValue(it)
                state.postValue(State.Success())
            }.onError {
                Log.e(TAG, it.message.toString())
                state.postValue(State.Error(it.message.toString()))
            }
    }


    fun setSearchSpinnerState(type: SearchType) {
        searchType = type
        if (searchQuery != null) getRecipes()
    }

    fun setSortSpinnerState(type: SortType) {
        sortType = type
        getRecipes()
    }

    fun setSearchQuery(query: String) {
        searchQuery = if (query.length >= SEARCH_MIN_QUERY) query else null
        getRecipes()
    }


}