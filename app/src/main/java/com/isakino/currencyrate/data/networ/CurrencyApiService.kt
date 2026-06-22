package com.isakino.currencyrate.data.networ

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("v6/latest/{base_currency}")
    suspend fun getLatestRates(
        @Path("base_currency") baseCurrency: String
    ): Response<CurrencyResponseDTO>
}
