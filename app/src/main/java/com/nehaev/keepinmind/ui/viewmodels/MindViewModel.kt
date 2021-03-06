package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.Resource
import kotlinx.coroutines.launch

class MindViewModel(
        val mindRepository: MindRepository
) : ViewModel() {

    val testsLiveData: MutableLiveData<Resource<List<Test>>> = MutableLiveData()
    lateinit var testsDbLiveData: LiveData<List<Test>>

    val themeViewModel = ThemeViewModel(mindRepository, viewModelScope)

    init {
        getTests()
    }

    //fun getThemeViewModel() = ThemeViewModel(mindRepository, viewModelScope)

    fun getTests() = viewModelScope.launch {
        testsLiveData.postValue(Resource.Loading())
        val response = mindRepository.getAllTests()
        testsLiveData.postValue(handleTestsResponse(response))

        testsDbLiveData = mindRepository.getAllTestWithLiveData()
    }

    fun upsertTest(test: Test) {
        viewModelScope.launch {
            mindRepository.upsertTest(test)
        }
    }

    private fun handleTestsResponse(response: List<Test>): Resource<List<Test>> {
        response?.let {resultResponse ->
            return Resource.Success(resultResponse)
        }
        return Resource.Error("handle tests error")
    }
}