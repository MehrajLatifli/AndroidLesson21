package com.example.androidlesson21.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlesson21.api.Resource
import com.example.androidlesson21.api.repositories.RecipeRepositories
import com.example.androidlesson21.models.get.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repo: RecipeRepositories) : ViewModel() {

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe

    val isLoading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun getRecipeById(id: Int) {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = repo.getRecipeById(id)

                withContext(Dispatchers.Main) {
                    when (response) {
                        is Resource.Success -> {
                            val recipeResponse = response.data
                            if (recipeResponse != null) {
                                _recipe.value = recipeResponse!!

                            } else {
                                error.value = "No recipes found"
                            }
                        }
                        is Resource.Error -> {
                            error.value = "Failed to fetch recipes: ${response.message}"
                        }
                        else -> {

                        }
                    }


                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = e.localizedMessage ?: "An error occurred"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    loading.value = false
                }
            }
        }
    }


}