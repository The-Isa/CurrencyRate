package com.isakino.currencyrate.data.networ

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_currency_table")
data class HistoryCurrencyEntity(
    @PrimaryKey
    val code: String,       // Код валюты (например, "EUR")
    val rate: Double,       // Курс валюты на момент просмотра
    val timestamp: Long     // Время просмотра (нужно для правильной сортировки списка недавних)
)
