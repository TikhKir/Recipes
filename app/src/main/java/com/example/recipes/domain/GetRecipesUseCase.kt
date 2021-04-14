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

    private fun filterByName(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.name.capitalize().contains(searchQuery.capitalize()) }

    private fun filterByDescription(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.description.capitalize().contains(searchQuery.capitalize()) }

    private fun filterByInstructions(searchQuery: String, recipes: List<Recipe>): List<Recipe> =
        recipes.filter { it.instructions.capitalize().contains(searchQuery.capitalize()) }


    suspend fun execute(searchQuery: String?, searchType: SearchType, sortType: SortType)
            : Result<List<Recipe>> = repository.getRecipes()
        .transformInside { list ->
            if (searchQuery != null)
                when (searchType) {
                    SearchType.ByName -> filterByName(searchQuery, list)
                    SearchType.ByDescription -> filterByDescription(searchQuery, list)
                    SearchType.ByInstruction -> filterByInstructions(searchQuery, list)
                } else list
        }


    //здесь (вместе с интерфейсом, ага) должно происходиь все преобразование.
    //и обращаться к нему нужно из вьюмодели
    //а тут (внутри юзкейса) должно идти обращеие к репозиторию

    //а еще  надо в этот пакет бы перенеси ui-модели
}