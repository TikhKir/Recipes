package com.example.recipes.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "similar_recipes",
    indices = [Index(value = ["uuid", "parentUUID"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = RecipeDB::class,
            parentColumns = ["localRecipeId"],
            childColumns = ["parentUUID"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class SimilarRecipeDB(
    val uuid: String,
    val name: String,
    val imageURL: String,
    val parentUUID: String,
    @PrimaryKey(autoGenerate = true)
    val localSimilarId: Int? = null,
)
