package com.isakino.currencyrate.data.networ

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// В массив entities добавлены все три наши таблицы: Кэш, Избранное и История
@Database(
    entities = [
        CurrencyEntity::class,
        FavoriteCurrencyEntity::class,
        HistoryCurrencyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Функция, которая отдает интерфейс со всеми SQL-запросами
    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Метод для создания или получения синглтона базы данных
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
