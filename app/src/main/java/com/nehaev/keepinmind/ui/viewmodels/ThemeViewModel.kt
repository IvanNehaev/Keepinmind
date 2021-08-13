package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ThemeResource
import com.nehaev.keepinmind.util.ThemesItemListHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ThemeViewModel(
        private val mindRepository: MindRepository,
        private val viewModelScope: CoroutineScope
){

    val liveData: MutableLiveData<ThemeResource> = MutableLiveData()

    fun attach() {
        getThemes()
    }

    fun detach() {
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
            return ThemeResource.Success(ThemesItemListHelper.listToResourcesList(response))
        }
        return ThemeResource.Error("handle themes error")
    }

    fun upsertTheme(theme: Theme) {
        viewModelScope.launch {
            mindRepository.themes.upsertTheme(theme)
        }
    }

    fun onDialogClick() {
        getThemes()
    }

    fun onListItemClickListener(theme: Theme) {

    }
}





















