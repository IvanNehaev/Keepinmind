package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "themes"
)
data class Theme(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val name: String,
    val questionCnt: Int
) : Serializable