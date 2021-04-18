package com.example.recipes.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.domain.model.Recipe
import com.example.recipes.repository.RecipeRepository
import com.example.recipes.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: RecipeRepository
) : ViewModel() {
    companion object {
        private const val TAG = "DETAILS_VIEW_MODEL"
    }

    private val detailedRecipe = MutableLiveData<Recipe>()
    val detailedRecipeLD: LiveData<Recipe> get() = detailedRecipe

    private val state = MutableLiveData<State>(State.Default())
    val stateLD: LiveData<State> get() = state

    fun getRecipe(uuid: String) = viewModelScope.launch(Dispatchers.IO) {
        state.postValue(State.Loading())
        repo.getRecipeByUUID(uuid)
            .onSuccess {
                detailedRecipe.postValue(it)
                state.postValue(State.Success())
            }.onError {
                state.postValue(State.Error(it.message.toString()))
                Log.e(TAG, it.printStackTrace().toString())
            }
    }
}