package com.example.androidlesson21.models.get


import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("recipes")
    val recipes: List<Recipe>?,
    @SerializedName("skip")
    val skip: Int?,
    @SerializedName("total")
    val total: Int?
)