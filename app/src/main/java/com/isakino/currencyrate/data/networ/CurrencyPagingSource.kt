package com.isakino.currencyrate.data.networ

import androidx.paging.PagingSource
import androidx.paging.PagingState

class CurrencyPagingSource(
    private val currencyDao: CurrencyDao
) : PagingSource<Int, FavoriteCurrencyEntity>() { // Изменили тип на FavoriteCurrencyEntity

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteCurrencyEntity> {
        return try {
            val currentPage = params.key ?: 0

            // Читаем данные напрямую из нашей новой независимой таблицы избранного
            val allFavorites = currencyDao.getFavoriteCurrencies()

            val fromIndex = currentPage * params.loadSize
            val toIndex = minOf(fromIndex + params.loadSize, allFavorites.size)

            val pageData = if (fromIndex < allFavorites.size) {
                allFavorites.subList(fromIndex, toIndex)
            } else {
                emptyList()
            }

            val prevKey = if (currentPage > 0) currentPage - 1 else null
            val nextKey = if (toIndex < allFavorites.size) currentPage + 1 else null

            LoadResult.Page(
                data = pageData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FavoriteCurrencyEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
