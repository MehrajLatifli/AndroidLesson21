package com.example.androidlesson21.api

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    data class Success<T>(val dataSuccess: T?) : Resource<T>(dataSuccess)

    data class Error<T>(val errorMessage: String?) : Resource<T>(null, errorMessage)

 //   data class Loading<T>(val isLoading: Boolean = true) : Resource<T>()

}