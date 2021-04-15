package com.example.recipes.domain

import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.filterparameters.SearchType
import com.example.recipes.utils.filterparameters.SortType

class RecipesHandleUseCase {
    companion object {

        suspend fun execute(
            recipeList: List<Recipe>,
            searchQuery: String?,
            searchType: SearchType,
            sortType: SortType
        ): List<Recipe> = recipeList
            .strictSearch(searchQuery, searchType)
            .sortByParameters(sortType)


        private suspend fun List<Recipe>.strictSearch(searchQuery: String?, searchType: SearchType)
                : List<Recipe> =
            if (searchQuery != null)
                when (searchType) {
                    SearchType.ByName -> filterByName(searchQuery, this)
                    SearchType.ByDescription -> filterByDescription(searchQuery, this)
                    SearchType.ByInstruction -> filterByInstructions(searchQuery, this)
                } else this

        private suspend fun List<Recipe>.sortByParameters(sortType: SortType): List<Recipe> =
            when (sortType) {
                SortType.Unsorted -> this
                SortType.ByNameAsc -> this.sortedBy { it.name }
                SortType.ByNameDesc -> this.sortedByDescending { it.name }
                SortType.ByLastUpdateAsc -> this.sortedBy { it.lastUpdated }
                SortType.ByLastUpdateDesc -> this.sortedByDescending { it.lastUpdated }
            }

        private suspend fun filterByName(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
            recipes.filter { it.name.capitalize().contains(searchQuery.capitalize()) }

        private suspend fun filterByDescription(
            searchQuery: String,
            recipes: List<Recipe>
        ): List<Recipe> =
            recipes.filter { it.description.capitalize().contains(searchQuery.capitalize()) }

        private suspend fun filterByInstructions(
            searchQuery: String,
            recipes: List<Recipe>
        ): List<Recipe> =
            recipes.filter { it.instructions.capitalize().contains(searchQuery.capitalize()) }

    }
}