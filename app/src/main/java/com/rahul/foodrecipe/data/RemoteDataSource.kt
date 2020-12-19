package com.rahul.foodrecipe.data

import com.rahul.foodrecipe.data.network.FoodRecipeApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {
    suspend fun getRecipes(queries: Map<String, String>) = foodRecipeApi.getRecipes(queries)
}