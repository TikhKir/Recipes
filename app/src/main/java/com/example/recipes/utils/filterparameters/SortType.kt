package com.example.recipes.utils.filterparameters

sealed class SortType {
    object Missing: SortType()
    object ByNameAsc: SortType()
    object ByNameDesc: SortType()
    object ByLastUpdateAsc: SortType()
    object ByLastUpdateDesc: SortType()
}
