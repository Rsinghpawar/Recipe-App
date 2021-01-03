package com.rahul.foodrecipe.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rahul.foodrecipe.models.FoodRecipes
import com.rahul.foodrecipe.utils.Constants.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipes: FoodRecipes
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}