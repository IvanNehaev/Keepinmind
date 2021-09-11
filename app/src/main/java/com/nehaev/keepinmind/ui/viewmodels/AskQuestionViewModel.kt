package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.repository.MindRepository
import com.nehaev.keepinmind.util.ViewModelFactoryContainer
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AskQuestionViewModel(
    val mindRepository: MindRepository,
    private val testContainer: ViewModelFactoryContainer<Test>
) : ViewModel() {
    val liveData: MutableLiveData<AskQuestionViewState> = MutableLiveData()
    private val mQuestionsList = mutableListOf<Question>()
    private val mTest: Test by lazy {
       testContainer.data
    }

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() = viewModelScope.launch {
        // Set loading state in view
        liveData.postValue(AskQuestionViewState.Loading())
        // Load selected themes id from DB
        val selectedThemesIdList =
            async { mindRepository.selectedThemesRepository.getThemesId(mTest.itemTableName) }
        // Load all questions from selected themes
        selectedThemesIdList.await().forEach { id ->
            mQuestionsList += async { mindRepository.questions.getAllQuestionsFromTheme(id) }.await()
        }
        // Send loaded questions to view
        liveData.postValue(AskQuestionViewState.QuestionsLoaded(mQuestionsList))
    }

    sealed class AskQuestionViewState(
        val questionsList: List<Question>? = null
    ) {
        class Loading() : AskQuestionViewState()
        class QuestionsLoaded(questions: List<Question>) :
            AskQuestionViewState(questionsList = questions)
    }
}