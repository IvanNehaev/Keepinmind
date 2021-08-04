package com.nehaev.keepinmind.ui.viewmodels

import android.util.Log
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

    private val TAG = QuestionCreateViewModel::class.simpleName

    sealed class ViewStates() {
        class ValidQuestion : ViewStates()
        class InvalidQuestion : ViewStates()
        class SaveQuestion : ViewStates()
    }

    var questionText: String = ""
    var answerText: String = ""
    var theme: Theme? = null
    var themeId: String = ""
        set(value) {
            // get theme from db
            viewModelScope.launch {
                theme = mindRepository.themes.getThemeById(value)
            }
        }
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

        theme?.let { theme->
            // create new question or update existed
            val newQuestion = updateQuestion()
            // write question to DB and update theme question count
            viewModelScope.launch {
                mindRepository
                    .questions
                    .upsertQuestion(newQuestion)
                // update question count if new question has been created
                if (question == null)
                    updateThemeQuestionsCount(theme)
            }

            // set save question state on view
            liveData.postValue(ViewStates.SaveQuestion())
        }

    }

    private fun updateQuestion() =
        if (question != null)
            Question(
                id = question!!.id,
                themeId = question!!.themeId,
                answer = answerText,
                question = questionText
            )
        else
            Question(
                id = UUID.randomUUID().toString(),
                themeId = theme!!.id,
                answer = answerText,
                question = questionText
            )

    private fun updateQuestion(question: Question) = Question(
        id = question.id,
        themeId = question.themeId,
        answer = answerText,
        question = questionText
    )

    private suspend fun updateThemeQuestionsCount(theme: Theme) {

        // create new theme with updated question counter
        var updatedTheme = Theme(
            id = theme.id,
            categoryId = theme.categoryId,
            categoryName = theme.categoryName,
            name = theme.name,
            questionCnt = theme.questionCnt + 1
        )
        // send updated theme to DB
        mindRepository.themes.upsertTheme(updatedTheme)
    }
}