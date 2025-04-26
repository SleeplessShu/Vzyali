package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VacancyLongDbEntity::class],
    version = 1,
    exportSchema = true
)
abstract class FavoriteVacanciesDatabase : RoomDatabase() {
    abstract fun getVacancyDao(): VacancyDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteVacanciesDatabase? = null
    }
}
