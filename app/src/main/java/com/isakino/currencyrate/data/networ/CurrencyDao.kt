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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)


    // ========================================================================
    // 2. ИЗБРАННОЕ
    // ========================================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(currency: FavoriteCurrencyEntity)

    @Delete
    suspend fun deleteFavorite(currency: FavoriteCurrencyEntity)

    @Query("SELECT * FROM favorite_currency_table")
    suspend fun getFavoriteCurrencies(): List<FavoriteCurrencyEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currency_table WHERE code = :currencyCode)")
    suspend fun isFavorite(currencyCode: String): Boolean


    // ========================================================================
    // 3. ИСТОРИЯ
    // ========================================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryCurrencyEntity)

    @Query("SELECT * FROM history_currency_table ORDER BY timestamp DESC")
    suspend fun getHistoryCurrencies(): List<HistoryCurrencyEntity>


    // ========================================================================
    // 4. УВЕДОМЛЕНИЯ
    // ========================================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notification_table ORDER BY triggerTime ASC")
    suspend fun getNotifications(): List<NotificationEntity>
}