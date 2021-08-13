package com.nehaev.keepinmind.repository

import com.nehaev.keepinmind.db.TestDao
import com.nehaev.keepinmind.models.Test

class TestsRepository(
    private val testsDao: TestDao
    ) {

    suspend fun upsert(test: Test) {
        testsDao.upsert(test)
    }

    suspend fun getAllTests(): List<Test> = testsDao.getAllTests()

    suspend fun delete(test: Test) {
        testsDao.deleteTest(test)
    }
}