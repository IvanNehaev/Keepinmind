package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import com.nehaev.keepinmind.util.ThemeResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ThemeViewModel(
        private val mindRepository: MindRepository,
        private val viewModelScope: CoroutineScope
) {

    val liveData: MutableLiveData<ThemeResource> = MutableLiveData()

    init {
        getThemes()
    }

    fun getThemes() = viewModelScope.launch {
        liveData.postValue(ThemeResource.Loading())
        val response = mindRepository.themes.getAllThemes()
        liveData.postValue(handleThemesResponse(response))
    }

    private fun handleThemesResponse(response: LiveData<List<Theme>>): ThemeResource {
        response?.let {resultResponse ->
            return ThemeResource.Success(resultResponse)
        }
        return ThemeResource.Error("handle themes error")
    }
}