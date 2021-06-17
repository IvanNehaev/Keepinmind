package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "themes"
)
data class Theme(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoryId: Int,
    val name: String,
    val questionCnt: Int
)