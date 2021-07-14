package com.nehaev.keepinmind.repository

import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.CategoryDao
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Theme

class CategoryRepository(
        private val categoryDao: CategoryDao
) {
      suspend fun getAllCategorise(): List<Category> = categoryDao.getAllCategories()

    suspend fun upsertCategory(category: Category) {
        categoryDao.upsert(category)
    }
}