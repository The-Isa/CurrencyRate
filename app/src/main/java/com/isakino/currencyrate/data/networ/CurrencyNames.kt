package com.isakino.currencyrate.data.networ

import java.util.Currency
import java.util.Locale

object CurrencyNames {

    fun getName(code: String): String {

        return try {

            val currency = Currency.getInstance(code.uppercase())

            // Русское название валюты
            currency.getDisplayName(Locale("ru"))

        } catch (e: Exception) {

            // Если валюта неизвестна, показываем её код
            code
        }
    }
}