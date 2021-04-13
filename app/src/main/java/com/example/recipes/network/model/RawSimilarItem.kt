package com.example.recipes.network.model

import com.google.gson.annotations.SerializedName

data class RawSimilarItem(

    @SerializedName("image")
    val image: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("uuid")
    val uuid: String = ""
)