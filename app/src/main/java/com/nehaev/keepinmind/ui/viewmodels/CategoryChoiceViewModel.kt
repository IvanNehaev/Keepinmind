package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CategoryChoiceViewModel(
    private val mindRepository: MindRepository,
    private val viewModelScope: CoroutineScope
) {

    val liveData: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

    fun attach() {
        getCategories()
    }

    fun detach() {
    }

    private fun getCategories() = viewModelScope.launch {
        // set loading state on view
        liveData.postValue(Resource.Loading())
        // get all categories from db
        var response = listOf<Category>()
        async { response = mindRepository.categories.getAllCategorise() }.await()
        // send response to view
        liveData.postValue(handleCategoryResponse(response))
    }

    private fun handleCategoryResponse(response: List<Category>): Resource<List<Category>> {
        val categories = mutableListOf<Category>()
        // add item "new category" in category list
        categories.add(
            Category(
                id = 0,
                name = "New category")
        )
        return Resource.Success(data = categories)
    }

    fun onButtonSaveClick(name: String, category: String) {
        if (name.isNotBlank() && category.isNotBlank()) {
            viewModelScope.launch {

            }
        }

    }
}

























