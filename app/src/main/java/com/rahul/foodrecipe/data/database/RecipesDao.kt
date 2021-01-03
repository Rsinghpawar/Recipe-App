package com.rahul.foodrecipe.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipesEntity: RecipesEntity)

    @Query("Select * from recipes_table Order by id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>

}