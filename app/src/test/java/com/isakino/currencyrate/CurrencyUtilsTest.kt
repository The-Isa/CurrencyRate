package com.isakino.currencyrate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun validRate_shouldReturnTrue() {
        assertTrue(
            CurrencyUtils.isValidRate(15.4)
        )
    }

    @Test
    fun invalidRate_shouldReturnFalse() {
        assertFalse(
            CurrencyUtils.isValidRate(0.0)
        )
    }

    @Test
    fun formatRate_shouldReturnFourDigits() {
        assertEquals(
            "12.3457",
            CurrencyUtils.formatRate(12.345678)
        )
    }

    @Test
    fun favorite_shouldReturnTrue() {

        val favorites = listOf(
            "USD",
            "EUR",
            "JPY"
        )

        assertTrue(
            CurrencyUtils.isFavorite(
                "EUR",
                favorites
            )
        )
    }

    @Test
    fun favorite_shouldReturnFalse() {

        val favorites = listOf(
            "USD",
            "EUR"
        )

        assertFalse(
            CurrencyUtils.isFavorite(
                "GBP",
                favorites
            )
        )
    }
}