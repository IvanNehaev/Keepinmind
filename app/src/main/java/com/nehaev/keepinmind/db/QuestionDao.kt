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

    @Delete
    suspend fun deleteQuestion(question: Question)
}