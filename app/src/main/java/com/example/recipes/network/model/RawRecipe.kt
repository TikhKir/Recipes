package com.example.recipes.network.model

import com.google.gson.annotations.SerializedName

data class RawRecipe(

    @SerializedName("difficulty")
    val difficulty: Int = 0,

    @SerializedName("lastUpdated")
    val lastUpdated: Int = 0,

    @SerializedName("instructions")
    val instructions: String = "",

    @SerializedName("similar")
    val rawSimilar: List<RawSimilarItem> = ArrayList(),

    @SerializedName("images")
    val images: List<String> = ArrayList(),

    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("uuid")
    val uuid: String = ""
)
