package com.nehaev.keepinmind.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories"
)
data class Category(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String
)