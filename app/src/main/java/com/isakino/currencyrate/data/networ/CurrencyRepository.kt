package com.isakino.currencyrate.data.networ

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CurrencyRepository(
    private val apiService: CurrencyApiService,
    private val currencyDao: CurrencyDao
) {

    // ========================================================================
    // 1. СЕТЬ И КЭШ
    // ========================================================================

    suspend fun fetchLatestRates(baseCurrency: String): Response<CurrencyResponseDTO> {
        return apiService.getLatestRates(baseCurrency)
    }

    suspend fun saveRatesToDatabase(ratesMap: Map<String, Double>) {
        val entities = ratesMap.map { (code, rate) ->
            CurrencyEntity(code = code, rate = rate)
        }
        currencyDao.insertAll(entities)
    }

    // ========================================================================
    // 2. ИЗБРАННОЕ
    // ========================================================================

    suspend fun addToFavorites(code: String, rate: Double) {
        currencyDao.insertFavorite(
            FavoriteCurrencyEntity(code, rate)
        )
    }

    suspend fun removeFromFavorites(code: String, rate: Double) {
        currencyDao.deleteFavorite(
            FavoriteCurrencyEntity(code, rate)
        )
    }

    suspend fun isCurrencyFavorite(code: String): Boolean {
        return currencyDao.isFavorite(code)
    }

    suspend fun getFavoriteCurrencies(): List<FavoriteCurrencyEntity> {
        return currencyDao.getFavoriteCurrencies()
    }

    fun getPagedFavoriteCurrencies(): Flow<PagingData<FavoriteCurrencyEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CurrencyPagingSource(currencyDao) }
        ).flow
    }

    // ========================================================================
    // 3. ИСТОРИЯ
    // ========================================================================

    suspend fun addToHistory(code: String, rate: Double) {

        currencyDao.insertHistoryItem(
            HistoryCurrencyEntity(
                code = code,
                rate = rate,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun getHistoryList(): List<HistoryCurrencyEntity> {
        return currencyDao.getHistoryCurrencies()
    }

    // ========================================================================
    // 4. УВЕДОМЛЕНИЯ
    // ========================================================================

    suspend fun addNotification(
        currencyCode: String,
        message: String,
        triggerTime: Long
    ) {

        currencyDao.insertNotification(
            NotificationEntity(
                currencyCode = currencyCode,
                message = message,
                triggerTime = triggerTime
            )
        )
    }

    suspend fun removeNotification(notification: NotificationEntity) {
        currencyDao.deleteNotification(notification)
    }

    suspend fun getNotifications(): List<NotificationEntity> {
        return currencyDao.getNotifications()
    }
}