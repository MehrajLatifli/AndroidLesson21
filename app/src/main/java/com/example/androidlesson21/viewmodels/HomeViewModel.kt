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
class HomeViewModel @Inject constructor(private val repo: RecipeRepositories) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private var originalRecipeList = listOf<Recipe>()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    fun getAllRecipes() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getAllRecipes()

                withContext(Dispatchers.Main) {
                    when (response) {
                        is Resource.Success -> {
                            val recipeResponse = response.data
                            if (recipeResponse != null && recipeResponse.recipes != null && recipeResponse.recipes.isNotEmpty()) {
                                _recipes.value = recipeResponse.recipes.filterNotNull()
                                originalRecipeList = recipeResponse.recipes.filterNotNull()
                            } else {
                                error.value = "No recipes found"
                                _recipes.value = emptyList()
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


    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            _recipes.value = originalRecipeList
            return
        }

        val filtered = originalRecipeList.filter { item ->
            item.name?.contains(query, ignoreCase = true) ?: false
        }
        _recipes.value = filtered
    }
}
