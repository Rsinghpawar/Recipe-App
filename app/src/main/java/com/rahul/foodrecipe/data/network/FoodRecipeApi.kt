package com.rahul.foodrecipe.data.network

import com.rahul.foodrecipe.models.FoodRecipes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries : Map<String,String>
    ) : Response<FoodRecipes>
}