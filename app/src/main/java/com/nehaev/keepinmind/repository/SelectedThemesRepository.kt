package com.nehaev.keepinmind.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.provider.BaseColumns
import android.util.Log
import androidx.room.Ignore
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nehaev.keepinmind.db.MindDatabase
import com.nehaev.keepinmind.models.Theme

class SelectedThemesRepository(
    val db: MindDatabase
) {

    companion object {
        private val TAG = SelectedThemesRepository.javaClass.simpleName
        private const val COL_ID = BaseColumns._ID
        private const val COL_THEME_ID = "themeid"
        private const val TABLE_NAME_PLACEHOLDER = ":tablename:"
        private const val TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME_PLACEHOLDER +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_THEME_ID + " TEXT)"
    }

    suspend fun addTable(name: String): Boolean {
        val sdb = db.openHelper.writableDatabase
        try {
            sdb.execSQL(TABLE_CREATE_SQL.replace(TABLE_NAME_PLACEHOLDER, name))
        } catch (e: SQLiteException) {
            return false
        }
        return true
    }

    suspend fun insertTheme(tableName: String, themeId: String) {
        val sdb = db.openHelper.writableDatabase
        insertRow(
            sdb = sdb,
            tableName = tableName,
            themeId = themeId
        )
    }

    suspend fun insertThemes(tableName: String, themes: List<Theme>) {
        Log.d(TAG, tableName)
        themes.map {
            insertTheme(tableName, it.id)
        }
    }

    suspend fun getRows(tableName: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val cursor = db.openHelper.writableDatabase.query("select * from $tableName")

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            result.put(
                cursor.getString(
                    cursor.getColumnIndex(COL_ID)
                ),
                cursor.getString(
                    cursor.getColumnIndex(COL_THEME_ID)
                )
            )
            cursor.moveToNext()
        }
        return result
    }

    suspend fun getThemesId(tableName: String): List<String> {
        val result = mutableListOf<String>()
        val cursor = db.openHelper.writableDatabase.query("select * from $tableName")

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            result.add(
                cursor.getString(
                    cursor.getColumnIndex(COL_THEME_ID)
                )
            )
            cursor.moveToNext()
        }
        return result
    }

    suspend fun clearTable(tableName: String) {
        db.openHelper.writableDatabase.execSQL("delete from $tableName")
    }

    suspend fun deleteTable(tableName: String) {
        try {
            Log.d(TAG, "Delete $tableName")
            db.openHelper.writableDatabase.execSQL("drop table if exists $tableName")
        } catch (e: SQLiteException) {
            Log.d(TAG, e.toString())
        }
    }

    suspend fun removeThemeFromSelected(theme: Theme) {
        val sdb = db.openHelper.writableDatabase
        val testsList = db.getTestDao().getAllTests()
        testsList.forEach { test ->
            Log.d(TAG, "Test name ${test.name}")
            val selectedThemesMap = getRows(test.itemTableName)
            selectedThemesMap.forEach {
                Log.d(TAG, "Id: ${it.key}, ThemeId: ${it.value}")
            }
            if (selectedThemesMap.containsValue(theme.id)) {
                selectedThemesMap.forEach {
                    if (it.value == theme.id) {
                        sdb.execSQL("delete from ${test.itemTableName} WHERE $COL_ID = ${it.key}")
                    }
                }
            }
        }
    }

    private suspend fun insertRow(
        sdb: SupportSQLiteDatabase,
        tableName: String,
        themeId: String
    ): Long {
        val cv = ContentValues()
        cv.put(COL_THEME_ID, themeId)
        return sdb.insert(tableName, OnConflictStrategy.REPLACE, cv)
    }

}