package com.example.recipes.utils.filterparameters

sealed class SearchType {
    object Missing: SearchType()
    object ByName: SearchType()
    object ByDescription: SearchType()
    object ByInstruction: SearchType()
}
