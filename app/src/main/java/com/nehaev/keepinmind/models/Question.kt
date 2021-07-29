package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "questions"
)
data class Question(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val themeId: String,
    val question: String,
    val answer: String
) : Serializable