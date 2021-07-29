package com.nehaev.keepinmind.ui.viewmodels

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.QuestionsFragmentStates
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuestionsViewModel(
    val mindRepository: MindRepository,
    val theme: Theme
) : ViewModel() {

    val liveData: MutableLiveData<QuestionsFragmentStates> = MutableLiveData()

    init {
        // get all questions from Db
        getQuestions()
    }

    private fun getQuestions() = viewModelScope.launch {
        // set loading state on view
        liveData.postValue(QuestionsFragmentStates.Loading())
        // get all questions from db
        var response = listOf<Question>()
        async { response = mindRepository.questions.getAllQuestionsFromTheme(theme) }.await()

        // send response to view
        if (response.isEmpty())
            liveData.postValue(QuestionsFragmentStates.EmptyList())
        else
            liveData.postValue(QuestionsFragmentStates.Success(response))
    }
}