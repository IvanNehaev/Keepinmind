package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions"
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val themeId: Int,
    val question: String,
    val answer: String
)