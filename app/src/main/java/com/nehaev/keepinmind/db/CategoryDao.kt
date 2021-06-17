package com.nehaev.keepinmind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nehaev.keepinmind.models.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: Category): Long

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Delete
    suspend fun deleteCategory(category: Category)
}