package com.nehaev.keepinmind.repository

import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.ThemeDao
import com.nehaev.keepinmind.models.Theme

class ThemeRepository(
        private val themeDao: ThemeDao
) {

    suspend fun getAllThemes(): List<Theme> = themeDao.getAllThemes()

    suspend fun upsertTheme(theme: Theme) {
        themeDao.upsert(theme)
    }
}