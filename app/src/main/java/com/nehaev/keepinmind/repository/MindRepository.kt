package com.nehaev.keepinmind.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.models.Theme

class MindRepository(
    val db: MindDatabase
) {

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

    suspend fun refreshDataInTests() {
        val testsList = tests.getAllTests()

        testsList.forEach { test ->
            val themesIdList = selectedThemesRepository.getThemesId(test.itemTableName)
            val selectedThemesList = mutableListOf<Theme>()
            themesIdList.forEach { themeId ->
                val theme = themes.getThemeById(themeId)
                selectedThemesList.add(theme)
            }
            val questionCount = selectedThemesList.fold(0) { accumulator, theme ->
                accumulator + theme.questionCnt
            }
            val newTest =
                Test(
                    id = test.id,
                    name = test.name,
                    questionCnt = questionCount,
                    rate = test.rate,
                    itemTableName = test.itemTableName
                )
            tests.upsert(newTest)
        }
    }

    suspend fun refreshDataInTestsWithTheme(editedTheme: Theme) {
        val testsList = tests.getAllTests()

        testsList.forEach { test ->
            val themesIdList = selectedThemesRepository.getThemesId(test.itemTableName)
            if (themesIdList.contains(editedTheme.id)) {
                val selectedThemesList = mutableListOf<Theme>()
                themesIdList.forEach { themeId ->
                    val theme = themes.getThemeById(themeId)
                    selectedThemesList.add(theme)
                }
                val questionCount = selectedThemesList.fold(0) { accumulator, theme ->
                    accumulator + theme.questionCnt
                }
                val newTest =
                    Test(
                        id = test.id,
                        name = test.name,
                        questionCnt = questionCount,
                        rate = test.rate,
                        itemTableName = test.itemTableName
                    )
                tests.upsert(newTest)
            }
        }
    }
}