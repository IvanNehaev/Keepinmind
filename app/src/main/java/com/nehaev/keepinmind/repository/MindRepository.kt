package com.nehaev.keepinmind.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Test

class MindRepository(
        val db: MindDatabase
)  {

    val themes = ThemeRepository(db.getThemeDao())
    val categories = CategoryRepository(db.getCategoryDao())
    val questions = QuestionsRepository(db.getQuestionDao())
    val tests = TestsRepository(db.getTestDao())
    val selectedThemesRepository = SelectedThemesRepository(db)

    suspend fun getAllTests(): List<Test> =
        db.getTestDao().getAllTests()

    fun getAllTestWithLiveData(): LiveData<List<Test>> = db.getTestDao().getAllTestsWithLiveData()

    suspend fun upsertTest(test: Test) {
        db.getTestDao().upsert(test)
    }
}