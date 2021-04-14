package com.example.recipes.utils.filterparameters

sealed class SortType {
    object Unsorted: SortType()
    object ByNameAsc: SortType()
    object ByNameDesc: SortType()
    object ByLastUpdateAsc: SortType()
    object ByLastUpdateDesc: SortType()
}
