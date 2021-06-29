package com.nehaev.keepinmind.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Test

class MindRepository(
        val db: MindDatabase
)  {
    suspend fun getAllTests(): List<Test> =
        db.getTestDao().getAllTests()

    fun getAllTestWithLiveData(): LiveData<List<Test>> = db.getTestDao().getAllTestsWithLiveData()

    suspend fun upsertTest(test: Test) {
        db.getTestDao().upsert(test)
    }
}