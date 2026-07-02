package com.isakino.currencyrate

import java.util.Locale

object CurrencyUtils {

    fun isValidRate(rate: Double): Boolean {
        return rate > 0
    }

    fun formatRate(rate: Double): String {
        return String.format(Locale.US, "%.4f", rate)
    }

    fun isFavorite(
        code: String,
        favorites: List<String>
    ): Boolean {
        return favorites.contains(code)
    }
}