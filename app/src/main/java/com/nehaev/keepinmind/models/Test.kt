package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "tests"
)
data class Test(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val itemTableName: String,
    val name: String,
    val questionCnt: Int,
    val rate: Int
) : Serializable