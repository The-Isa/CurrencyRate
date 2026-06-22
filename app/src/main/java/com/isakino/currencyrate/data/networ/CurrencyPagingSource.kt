package com.isakino.currencyrate.data.networ

import androidx.paging.PagingSource
import androidx.paging.PagingState

class CurrencyPagingSource(
    private val currencyDao: CurrencyDao
) : PagingSource<Int, CurrencyEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CurrencyEntity> {
        return try {
            // Текущая страница (если первая загрузка, то ставим 0)
            val currentPage = params.key ?: 0

            // Запрашиваем из Избранного или общей базы данных список валют
            // Так как у нас простая БД, мы берем сохраненные данные
            val allFavorites = currencyDao.getFavoriteCurrencies()

            // Имитируем пагинацию: делим список на страницы по размеру params.loadSize (например, по 20 элементов)
            val fromIndex = currentPage * params.loadSize
            val toIndex = minOf(fromIndex + params.loadSize, allFavorites.size)

            val pageData = if (fromIndex < allFavorites.size) {
                allFavorites.subList(fromIndex, toIndex)
            } else {
                emptyList()
            }

            // Высчитываем ссылки на предыдущую и следующую страницы
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

    override fun getRefreshKey(state: PagingState<Int, CurrencyEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
