package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ThemeListResource
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ThemeViewModel(
        private val mindRepository: MindRepository,
        private val viewModelScope: CoroutineScope
) {

    val liveData: MutableLiveData<ThemeResource> = MutableLiveData()
    private var themesLiveData: LiveData<List<Theme>>? = null

    private val listItems = mutableListOf<ThemeListResource>()

    private val observer = Observer<List<Theme>> {
        onNewData(it)
    }

    fun attach() {
        getThemes()
    }

    fun detach() {
        themesLiveData?.removeObserver(observer)
    }

    private fun onNewData(themes: List<Theme>) {
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
    }

    fun getThemes() = viewModelScope.launch {
        liveData.postValue(ThemeResource.Loading())

        val response = mindRepository.themes.getAllThemes()
        response?.observeForever(observer)
        themesLiveData = response


        //val response = mindRepository.themes.getAllThemes()
        //liveData.postValue(handleThemesResponse(response))
    }

    private fun handleThemesResponse(response: LiveData<List<Theme>>): ThemeResource {
        response?.let {resultResponse ->
            return ThemeResource.Success(resultResponse)
        }
        return ThemeResource.Error("handle themes error")
    }

}