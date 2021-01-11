package com.rahul.foodrecipe.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.rahul.foodrecipe.utils.Constants.DEFAULT_DIET_TYPE
import com.rahul.foodrecipe.utils.Constants.DEFAULT_MEAL_TYPE
import com.rahul.foodrecipe.utils.Constants.PREFERENCES_DIET_TYPE
import com.rahul.foodrecipe.utils.Constants.PREFERENCES_DIET_TYPE_ID
import com.rahul.foodrecipe.utils.Constants.PREFERENCES_MEAL_TYPE
import com.rahul.foodrecipe.utils.Constants.PREFERENCES_MEAL_TYPE_ID
import com.rahul.foodrecipe.utils.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {

    private object PreferencesKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
    }

    private val dataStore = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(mealType: String, mealId: Int, dietType: String, dietId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedMealType] = mealType
            preferences[PreferencesKeys.selectedDietType] = dietType
            preferences[PreferencesKeys.selectedDietTypeId] = dietId
            preferences[PreferencesKeys.selectedMealTypeId] = mealId
        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data
        .catch { e ->
            if (e is IOException){
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map {
            val selectedMealType = it[PreferencesKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = it[PreferencesKeys.selectedMealTypeId] ?: 0
            val selectedDietType = it[PreferencesKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = it[PreferencesKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)