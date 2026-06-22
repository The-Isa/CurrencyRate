package com.isakino.currencyrate.data.networ

import com.google.gson.annotations.SerializedName

data class CurrencyResponseDTO(
    @SerializedName("result")
    val result: String,

    @SerializedName("base_code")
    val baseCode: String,

    @SerializedName("rates")
    val rates: Map<String, Double>
)
