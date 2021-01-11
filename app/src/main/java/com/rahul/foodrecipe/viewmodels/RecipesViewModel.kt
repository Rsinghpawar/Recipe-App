package com.rahul.foodrecipe.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.foodrecipe.data.DataStoreRepository
import com.rahul.foodrecipe.utils.Constants
import com.rahul.foodrecipe.utils.Constants.API_KEY
import com.rahul.foodrecipe.utils.Constants.DEFAULT_DIET_TYPE
import com.rahul.foodrecipe.utils.Constants.DEFAULT_MEAL_TYPE
import com.rahul.foodrecipe.utils.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.rahul.foodrecipe.utils.Constants.QUERY_API_KEY
import com.rahul.foodrecipe.utils.Constants.QUERY_DIET
import com.rahul.foodrecipe.utils.Constants.QUERY_FILL_INGREDIENTS
import com.rahul.foodrecipe.utils.Constants.QUERY_NUMBER
import com.rahul.foodrecipe.utils.Constants.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(
        selectedMealType: String,
        selectedMealTypeId: Int,
        selectedDietType: String,
        selectedDietTypeId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(
            selectedMealType,
            selectedMealTypeId,
            selectedDietType,
            selectedDietTypeId
        )
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries

    }
}