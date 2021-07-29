package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TestCreateViewModel(
    val mindRepository: MindRepository
) : ViewModel() {

    val liveData: MutableLiveData<TestCreateStates> = MutableLiveData()

    init {
        // get all themes from Db
        getThemes()
    }

    private fun getThemes() = viewModelScope.launch {
        // set loading status on view
        liveData.postValue(TestCreateStates.Loading())
        // get all themes from db
        var response = listOf<Theme>()
        async { response = mindRepository.themes.getAllThemes() }.await()
        // send response to view
        if (response.isEmpty())
            liveData.postValue(TestCreateStates.EmptyList())
        else
            liveData.postValue(TestCreateStates.Success(response))

    }

    sealed class TestCreateStates(
        val themes: List<Theme>? = null
    ) {
        class Loading : TestCreateStates()
        class Success(themes: List<Theme>) : TestCreateStates(themes = themes)
        class EmptyList() : TestCreateStates()
    }
}