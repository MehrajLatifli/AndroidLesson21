package com.example.androidlesson21.api.repositories

import com.example.androidlesson21.api.IApiManager
import com.example.androidlesson21.api.Resource
import com.example.androidlesson21.models.get.Recipe
import com.example.androidlesson21.models.get.RecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RecipeRepositories @Inject constructor(private val api: IApiManager) {


/*
    suspend fun getAllRecipes()=safeApiRequest {
        api.getAllRecipes()
    }

    suspend fun getRecipeById(id: Int)=safeApiRequest{
         api.getRecipeById(id)
    }


 */

    suspend fun getAllRecipes(): Resource<RecipeResponse> {
        return safeApiRequest {
            api.getAllRecipes()
        }
    }


    suspend fun getRecipeById(id: Int): Resource<Recipe> {
        return safeApiRequest {
            api.getRecipeById(id)
        }
    }


    suspend fun <T> safeApiRequest(request: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {

                val response = request.invoke()

                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    Resource.Error(response.message())
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }
    }

}
