package com.rahul.foodrecipe.data

import com.rahul.foodrecipe.data.database.RecipesDao
import com.rahul.foodrecipe.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dao: RecipesDao) {

    suspend fun insertRecipes(recipe: RecipesEntity) {
        dao.insert(recipe)
    }

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return dao.readRecipes()
    }
}