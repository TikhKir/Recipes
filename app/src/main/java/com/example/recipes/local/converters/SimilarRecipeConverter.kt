package com.example.recipes.local.converters

import androidx.room.TypeConverter
import com.example.recipes.repository.model.SimilarRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SimilarRecipeConverter {
    private val gson = Gson()

    //todo: ???

    @TypeConverter
    fun stringToSimilarList(value: String): List<SimilarRecipe> {
        val listType: Type = object : TypeToken<List<SimilarRecipe>>() {}.type
        return gson.fromJson<List<SimilarRecipe>>(value, listType)
    }

    @TypeConverter fun similarToString(hourForecastList: List<SimilarRecipe>): String {
        return gson.toJson(hourForecastList)
    }

}