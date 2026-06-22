package com.isakino.currencyrate.data.networ

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Указываем, какие таблицы будут в нашей БД, и задаем версию
@Database(entities = [CurrencyEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Функция, которая будет отдавать нам интерфейс с SQL-запросами
    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Метод для создания или получения уже существующей базы данных (Синглтон)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "currency_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
