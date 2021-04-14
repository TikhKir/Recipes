package com.example.recipes.domain

import com.example.recipes.repository.RecipeRepository
import com.example.recipes.domain.model.Recipe
import com.example.recipes.utils.datatype.Result
import com.example.recipes.utils.filterparameters.SearchType
import com.example.recipes.utils.filterparameters.SortType
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend fun execute(searchQuery: String?, searchType: SearchType, sortType: SortType)
            : Result<List<Recipe>> = repository.getRecipes()
        .transformInside { list -> strictSearch(list, searchQuery, searchType) }
        .transformInside { list -> sortByParameters(list, sortType) }


    private fun strictSearch(list: List<Recipe>, searchQuery: String?, searchType: SearchType)
            : List<Recipe> =
        if (searchQuery != null)
            when (searchType) {
                SearchType.ByName -> filterByName(searchQuery, list)
                SearchType.ByDescription -> filterByDescription(searchQuery, list)
                SearchType.ByInstruction -> filterByInstructions(searchQuery, list)
            } else list

    private fun sortByParameters(list: List<Recipe>, sortType: SortType): List<Recipe> =
        when (sortType) {
            SortType.Unsorted -> list
            SortType.ByNameAsc -> list.sortedBy { it.name }
            SortType.ByNameDesc -> list.sortedByDescending { it.name }
            SortType.ByLastUpdateAsc -> list.sortedBy { it.lastUpdated }
            SortType.ByLastUpdateDesc -> list.sortedByDescending { it.lastUpdated }
        }

    private fun filterByName(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.name.capitalize().contains(searchQuery.capitalize()) }

    private fun filterByDescription(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.description.capitalize().contains(searchQuery.capitalize()) }

    private fun filterByInstructions(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.instructions.capitalize().contains(searchQuery.capitalize()) }

}