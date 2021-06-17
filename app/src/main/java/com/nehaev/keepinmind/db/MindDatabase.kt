package com.nehaev.keepinmind.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nehaev.keepinmind.models.Category
import com.nehaev.keepinmind.models.Question
import com.nehaev.keepinmind.models.Test
import com.nehaev.keepinmind.models.Theme

@Database(
    entities = [Category::class, Question::class, Test::class, Theme::class],
    version = 1
)
abstract class MindDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getQuestionDao(): QuestionDao

    abstract fun getTestDao(): TestDao

    abstract fun getThemeDao(): ThemeDao

    companion object {
        @Volatile
        private var instance: MindDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MindDatabase::class.java,
                "mind_db.db"
            ).build()
    }
}