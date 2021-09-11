package com.nehaev.keepinmind.repository

import com.nehaev.keepinmind.db.QuestionDao
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Theme

class QuestionsRepository(
    private val questionsDao: QuestionDao
) {

    suspend fun getAllQuestionsFromTheme(theme: Theme) = questionsDao.getAllQuestionsFromTheme(themeId = theme.id)

    suspend fun getAllQuestionsFromTheme(id: String) = questionsDao.getAllQuestionsFromTheme(themeId = id)

    suspend fun upsertQuestion(question: Question) {
        questionsDao.upsert(question)
    }

    suspend fun deleteQuestion(question: Question) {
        questionsDao.deleteQuestion(question)
    }

    suspend fun deleteAllQuestionInTheme(theme: Theme) {
        val questionsList = getAllQuestionsFromTheme(theme)
        questionsList.forEach {
            deleteQuestion(it)
        }
    }
}