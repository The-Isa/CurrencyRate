package com.isakino.currencyrate.data.networ

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    // ========================================================================
    // 1. РАЗДЕЛ: ОСНОВНОЙ СПИСОК ВАЛЮТ (ЛОКАЛЬНЫЙ КЭШ ИЗ СЕТИ)
    // ========================================================================

    // Сохранить все скачанные валюты в базу. Если валюта уже есть — обновить её курс
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)


    // ========================================================================
    // 2. РАЗДЕЛ: ВТОРАЯ ТАБЛИЦА (НЕЗАВИСИМОЕ ИЗБРАННОЕ ПО ТЗ)
    // ========================================================================

    // Добавить валюту в избранное. Если уже есть — обновить курс
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(currency: FavoriteCurrencyEntity)

    // Удалить валюту из избранного
    @Delete
    suspend fun deleteFavorite(currency: FavoriteCurrencyEntity)

    // Получить все избранные валюты
    @Query("SELECT * FROM favorite_currency_table")
    suspend fun getFavoriteCurrencies(): List<FavoriteCurrencyEntity>

    // Проверить, находится ли конкретная валюта в избранном (вернет true или false)
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currency_table WHERE code = :currencyCode)")
    suspend fun isFavorite(currencyCode: String): Boolean


    // ========================================================================
    // 3. РАЗДЕЛ: ТРЕТЬЯ ТАБЛИЦА (ИСТОРИЯ ПРОСМОТРОВ ПО ТЗ)
    // ========================================================================

    // Добавить или обновить элемент в истории просмотров
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryCurrencyEntity)

    // Получить список недавно просмотренных валют, отсортированный от свежих к старым
    @Query("SELECT * FROM history_currency_table ORDER BY timestamp DESC")
    suspend fun getHistoryCurrencies(): List<HistoryCurrencyEntity>
}
