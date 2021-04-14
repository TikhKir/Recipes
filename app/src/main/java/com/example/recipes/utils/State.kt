package com.example.recipes.utils

sealed class State {
    object Default : State()
    object Loading : State()
    class Error(val errorMessage: String) : State()
    object Success : State()
}