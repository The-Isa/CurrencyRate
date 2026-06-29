package com.isakino.currencyrate.data.networ

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    // Базовый адрес бесплатного ExchangeRate API
    private const val BASE_URL = "https://open.er-api.com/"

    // Настроенная библиотека Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Готовый сервис для отправки запросов в сеть
    val apiService: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }

    // Переменная для хранения экземпляра базы данных
    private var database: AppDatabase? = null

    // Метод для инициализации базы данных
    fun initializeDatabase(context: Context): CurrencyDao {
        if (database == null) {
            database = AppDatabase.getDatabase(context)
        }
        return database!!.currencyDao()
    }

    // Быстрый доступ к DAO
    val currencyDao: CurrencyDao
        get() = database?.currencyDao()
            ?: throw IllegalStateException(
                "База данных не инициализирована! Вызовите initializeDatabase(context) сначала."
            )
}