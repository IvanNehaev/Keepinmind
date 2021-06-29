package com.nehaev.keepinmind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nehaev.keepinmind.models.Test

@Dao
interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(test: Test): Long

    @Query("SELECT * FROM tests")
    suspend fun getAllTests(): List<Test>

    @Query("SELECT * FROM tests")
    fun getAllTestsWithLiveData(): LiveData<List<Test>>

    @Delete
    suspend fun deleteTest(test: Test)
}