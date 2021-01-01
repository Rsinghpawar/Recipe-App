package com.rahul.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rahul.foodrecipe.data.Repository
import com.rahul.foodrecipe.models.FoodRecipes
import com.rahul.foodrecipe.utils.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipeResponse(response)
            } catch (e: Exception) {

            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes>? {
        return when{
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("Api Limit Reached")
            response.body()!!.results.isNullOrEmpty() -> NetworkResult.Error("Recipes not found.")
            response.isSuccessful -> {
                val recipes = response.body()
                NetworkResult.Success(recipes!!)
            }
            else -> NetworkResult.Error(response.message())
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }


    }
}