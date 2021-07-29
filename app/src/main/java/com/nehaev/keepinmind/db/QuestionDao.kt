package com.nehaev.keepinmind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nehaev.keepinmind.models.Question

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(question: Question): Long

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query( "SELECT * FROM questions WHERE themeId = :themeId")
    suspend fun getAllQuestionsFromTheme(themeId: String): List<Question>

    @Delete
    suspend fun deleteQuestion(question: Question)
}