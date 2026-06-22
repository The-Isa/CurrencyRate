package com.isakino.currencyrate.data.networ

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,        // Код валюты (например, "USD")
    val rate: Double,        // Текущий курс валюты
    val isInFavorites: Boolean = false // Тот самый флаг для Избранного из ТЗ
)
