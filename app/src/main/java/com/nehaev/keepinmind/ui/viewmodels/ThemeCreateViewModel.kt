package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ThemeCreateViewModel(
    private val mindRepository: MindRepository,
    private val viewModelScope: CoroutineScope
) {

    val liveData: MutableLiveData<Resource<List<String>>> = MutableLiveData()

    fun attach() {
        getThemes()
    }

    fun detach() {
    }

    private fun getThemes() = viewModelScope.launch {
        // set loading state on view
        liveData.postValue(Resource.Loading())
        // get all themes from db
        var response = listOf<Theme>()
        async { response = mindRepository.themes.getAllThemes() }.await()
        // send response to view
        liveData.postValue(handleThemesResponse(response))
    }

    private fun handleThemesResponse(response: List<Theme>) = Resource.Success(getCategoryList(response))


    private fun getCategoryList(themes: List<Theme>): List<String> {
        // create set for categories store
        val categories = mutableSetOf<String>()
        // add all categories names in set
        for(theme in themes)
            categories.add(theme.categoryName)
        // transform set to list and return list of categories
        return  categories.toList()
    }

    fun onButtonSaveClick(name: String, category: String) {
        if (name.isNotBlank() && category.isNotBlank()) {
            viewModelScope.launch {

            }
        }

    }
}

























