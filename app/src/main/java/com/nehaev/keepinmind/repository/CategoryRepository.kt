package com.nehaev.keepinmind.repository

import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.CategoryDao
import com.nehaev.keepinmind.models.Category

class CategoryRepository(
        private val categoryDao: CategoryDao
) {
        fun getAllCategorise(): LiveData<List<Category>> = categoryDao.getAllCategories()
}