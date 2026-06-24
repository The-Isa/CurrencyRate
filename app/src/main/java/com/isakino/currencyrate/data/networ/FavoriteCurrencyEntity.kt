package com.isakino.currencyrate.data.networ

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_currency_table")
data class FavoriteCurrencyEntity(
    @PrimaryKey
    val code: String,  // Код валюты (например "USD") будет уникальным ключом
    val rate: Double   // Сохраняем курс на момент добавления в избранное
)
