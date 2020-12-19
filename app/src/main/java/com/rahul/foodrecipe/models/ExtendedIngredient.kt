package com.rahul.foodrecipe.models


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(
    val amount: Double,
    val consistency: String,
    val image: String,
    val name: String,
    val original: String,
    val unit: String
)