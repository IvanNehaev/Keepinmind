package com.nehaev.keepinmind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nehaev.keepinmind.models.Theme

@Dao
interface ThemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(theme: Theme): Long

    @Query("SELECT * FROM themes")
    suspend fun getAllThemes(): List<Theme>

    @Query("SELECT * FROM themes")
    fun getAllThemesLiveData(): LiveData<List<Theme>>

    @Delete
    suspend fun deleteTheme(theme: Theme)
}