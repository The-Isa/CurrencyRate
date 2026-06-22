package com.isakino.currencyrate.data.networ

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    // Сохранить все скачанные валюты в базу. Если валюта уже есть — обновить её курс
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    // Получить только те валюты, которые добавлены в Избранное (для вкладки Избранное)
    @Query("SELECT * FROM currency_table WHERE isInFavorites = 1")
    suspend fun getFavoriteCurrencies(): List<CurrencyEntity>

    // Обновить статус избранного (добавить или удалить) для конкретной валюты по её коду
    @Query("UPDATE currency_table SET isInFavorites = :isFavorite WHERE code = :currencyCode")
    suspend fun updateFavoriteStatus(currencyCode: String, isFavorite: Boolean)
}
