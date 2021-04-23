package com.example.recipes.utils.filterparameters

fun getSortType(position: Int): SortType = when (position) {
    1 -> SortType.ByNameAsc
    2 -> SortType.ByNameDesc
    3 -> SortType.ByLastUpdateAsc
    4 -> SortType.ByLastUpdateDesc
    else -> SortType.Unsorted
}

fun getSearchType(position: Int): SearchType =  when (position) {
    1 -> SearchType.ByDescription
    2 -> SearchType.ByInstruction
    else -> SearchType.ByName
}