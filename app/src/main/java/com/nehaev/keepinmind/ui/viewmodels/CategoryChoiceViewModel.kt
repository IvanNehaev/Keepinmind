package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val mindRepository: MindRepository
) : ViewModel() {

    val liveData: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

    init {
        getCategories()
    }

    fun updateCategoryList() {
        getCategories()
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
                id = "0",
                name = "New category")
        )
        categories += response
        return Resource.Success(data = categories)
    }

    fun onDialogSaveClick() {
        updateCategoryList()
    }
}

























