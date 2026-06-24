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
    // 1. СЕТЕВОЙ СЛОЙ И КЭШ
    // ========================================================================

    // Запрос в сеть за свежими курсами
    suspend fun fetchLatestRates(baseCurrency: String): Response<CurrencyResponseDTO> {
        return apiService.getLatestRates(baseCurrency)
    }

    // Сохранение скачанных из сети валют в кэш
    suspend fun saveRatesToDatabase(ratesMap: Map<String, Double>) {
        val entities = ratesMap.map { (code, rate) ->
            CurrencyEntity(code = code, rate = rate)
        }
        currencyDao.insertAll(entities)
    }

    // ========================================================================
    // 2. ИЗБРАННОЕ
    // ========================================================================

    // Добавление валюты в избранное
    suspend fun addToFavorites(code: String, rate: Double) {
        val favoriteItem = FavoriteCurrencyEntity(code = code, rate = rate)
        currencyDao.insertFavorite(favoriteItem)
    }

    // Удаление валюты из избранного
    suspend fun removeFromFavorites(code: String, rate: Double) {
        val favoriteItem = FavoriteCurrencyEntity(code = code, rate = rate)
        currencyDao.deleteFavorite(favoriteItem)
    }

    // Проверка: находится ли валюта в избранном прямо сейчас
    suspend fun isCurrencyFavorite(code: String): Boolean {
        return currencyDao.isFavorite(code)
    }

    // Поток пагинации для таблицы избранного
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
    // 3. НОВЫЙ РАЗДЕЛ: ИСТОРИЯ ПРОСМОТРОВ (НЕ ДАВНО ПРОСМОТРЕННЫЕ)
    // ========================================================================

    // Метод для добавления валюты в историю при клике на неё
    suspend fun addToHistory(code: String, rate: Double) {
        val historyItem = HistoryCurrencyEntity(
            code = code,
            rate = rate,
            timestamp = System.currentTimeMillis() // Фиксируем точное время клика
        )
        currencyDao.insertHistoryItem(historyItem)
    }

    // Метод для получения списка недавних просмотров
    suspend fun getHistoryList(): List<HistoryCurrencyEntity> {
        return currencyDao.getHistoryCurrencies()
    }
}
