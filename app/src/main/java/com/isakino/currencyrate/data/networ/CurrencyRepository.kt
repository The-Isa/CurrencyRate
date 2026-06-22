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

    // 1. Запрос в сеть за свежими курсами
    suspend fun fetchLatestRates(baseCurrency: String): Response<CurrencyResponseDTO> {
        return apiService.getLatestRates(baseCurrency)
    }

    // 2. Метод для сохранения скачанных из сети валют в базу данных
    suspend fun saveRatesToDatabase(ratesMap: Map<String, Double>) {
        val entities = ratesMap.map { (code, rate) ->
            CurrencyEntity(code = code, rate = rate, isInFavorites = false)
        }
        currencyDao.insertAll(entities)
    }

    // 3. Метод длч обновления статуса "Избранного" (лайка)
    suspend fun toggleFavorite(code: String, isFavorite: Boolean) {
        currencyDao.updateFavoriteStatus(code, isFavorite)
    }

    // 4. Главный метод пагинации: возвращает поток PagingData для списков
    fun getPagedFavoriteCurrencies(): Flow<PagingData<CurrencyEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,          // Количество элементов на одной странице
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CurrencyPagingSource(currencyDao) }
        ).flow
    }
}
