package com.nehaev.keepinmind.util

import com.nehaev.keepinmind.models.Question

sealed class QuestionsFragmentStates(
    val questionsList: List<Question>? = null
) {
    class Loading() : QuestionsFragmentStates()
    class EmptyList() : QuestionsFragmentStates()
    class Success(questions: List<Question>) : QuestionsFragmentStates(questionsList = questions)
    class Close(): QuestionsFragmentStates()
}