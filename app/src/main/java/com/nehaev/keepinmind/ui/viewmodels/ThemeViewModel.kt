package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ThemeListResource
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ThemeViewModel(
        private val mindRepository: MindRepository,
        private val viewModelScope: CoroutineScope
) {

    val liveData: MutableLiveData<ThemeResource> = MutableLiveData()

    fun attach() {
        getThemes()
    }

    fun detach() {
    }

    private fun prepareItemList(themes: List<Theme>): List<ThemeListResource> {
        val listItems = mutableListOf<ThemeListResource>()
        val categories = mutableSetOf<String>()
        // collect all unic categories names
        for(theme in themes)
            categories.add(theme.categoryName)

        for (category in categories) {
            // add category name in itemList
            listItems.add(ThemeListResource.CategoryItem(category))
            // get themes included in current category
            val themesInCategory = themes.filter { theme ->
                theme.categoryName == category
            }
            // add theme in itemList
            for(theme in themesInCategory) {
                listItems.add(ThemeListResource.ThemeItem(theme))
            }
        }
        return listItems
    }

    private fun getThemes() = viewModelScope.launch {
        // set loading state on view
        liveData.postValue(ThemeResource.Loading())
        // get all themes from db
        var response = listOf<Theme>()
        async { response = mindRepository.themes.getAllThemes() }.await()
        // send response to view
        liveData.postValue(handleThemesResponse(response))
    }

    private fun handleThemesResponse(response: List<Theme>): ThemeResource {
        response?.let {
            return ThemeResource.Success(prepareItemList(response))
        }
        return ThemeResource.Error("handle themes error")
    }

    fun upsertTheme(theme: Theme) {
        viewModelScope.launch {
            mindRepository.themes.upsertTheme(theme)
        }
    }
}





















