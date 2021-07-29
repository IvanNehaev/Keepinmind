package com.nehaev.keepinmind.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Theme
import com.nehaev.keepinmind.repository.MindRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class QuestionCreateViewModel(
    val mindRepository: MindRepository
) : ViewModel() {

    sealed class ViewStates() {
        class ValidQuestion: ViewStates()
        class InvalidQuestion: ViewStates()
        class SaveQuestion: ViewStates()
    }

    var questionText: String = ""
    var answerText: String = ""
    var theme: Theme? = null
    var question: Question? = null

    val liveData: MutableLiveData<ViewStates> = MutableLiveData()

    init {
        // set view state
        liveData.postValue(ViewStates.InvalidQuestion())
    }

    fun onQuestionTextChanged(text: String) {
        questionText = text
        if (text.isNotBlank() && answerText.isNotBlank())
            liveData.postValue(ViewStates.ValidQuestion())
        else
            liveData.postValue(ViewStates.InvalidQuestion())
    }

    fun onAnswerTextChanged(text: String) {
        answerText = text
        if (text.isNotBlank() && questionText.isNotBlank())
            liveData.postValue(ViewStates.ValidQuestion())
        else
            liveData.postValue(ViewStates.InvalidQuestion())
    }

    fun onButtonSaveClick() {
        theme?.let { theme ->

            val newQuestion = if (question != null)
                Question(id = question!!.id,
                    themeId = question!!.themeId,
                    answer = answerText,
                    question = questionText)
             else
                Question(id = UUID.randomUUID().toString(),
                    themeId = theme.id,
                    answer = answerText,
                    question = questionText)


//            val newQuestion = Question(id = UUID.randomUUID().toString(),
//                themeId = theme.id,
//                answer = answerText,
//                question = questionText)

            viewModelScope.launch {

                updateThemeQuestionsCount(theme)

                async { mindRepository.questions.upsertQuestion(newQuestion) }.await()

                liveData.postValue(ViewStates.SaveQuestion())
            }
        }
    }

    private fun updateQuestion(question: Question) = Question(id = question.id,
        themeId = question.themeId,
        answer = answerText,
        question = questionText)

    private suspend fun updateThemeQuestionsCount(theme: Theme) {
        var updatedTheme = Theme(
            id = theme.id,
            categoryId = theme.categoryId,
            categoryName = theme.categoryName,
            name = theme.name,
            questionCnt = theme.questionCnt + 1
        )
        mindRepository.themes.upsertTheme(updatedTheme)
    }
}