package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tests"
)
data class Test(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val itemTableName: String,
    val name: String,
    val questionCnt: Int,
    val rate: Int
)