package com.example.recipes.local.converters

import androidx.room.TypeConverter

class ImageConverter {

    @TypeConverter
    fun toString(images: List<String>): String {
        return images.joinToString("|||")
    }

    @TypeConverter
    fun toImageList(str: String): List<String> {
        return str.split("|||")
    }

}