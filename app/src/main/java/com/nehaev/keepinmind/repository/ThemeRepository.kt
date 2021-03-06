package com.nehaev.keepinmind.repository

import androidx.lifecycle.LiveData
import com.nehaev.keepinmind.db.ThemeDao
import com.nehaev.keepinmind.models.Theme

class ThemeRepository(
        private val themeDao: ThemeDao
) {

    suspend fun getAllThemes(): List<Theme> = themeDao.getAllThemes()

    suspend fun getThemeById(id: String): Theme = themeDao.getThemeById(id)

    suspend fun upsertTheme(theme: Theme) {
        themeDao.upsert(theme)
    }

    suspend fun deleteTheme(theme: Theme) {
        themeDao.deleteTheme(theme)
    }
}