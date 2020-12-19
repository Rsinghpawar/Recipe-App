package com.rahul.foodrecipe.utils

sealed class NetworkResult<T>(
    data : T? = null,
    message : String?=null
){
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?) : NetworkResult<T>(message = message)
    class Loading<T> : NetworkResult<T>()
}
