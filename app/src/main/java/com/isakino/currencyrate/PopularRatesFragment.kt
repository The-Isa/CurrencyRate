package com.isakino.currencyrate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.CurrencyAdapter
import com.isakino.currencyrate.data.networ.CurrencyNames
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch

class PopularRatesFragment : Fragment() {

    private val horizontalAdapter = CurrencyAdapter()
    private val verticalAdapter = CurrencyAdapter()

    private var allRatesMap: Map<String, Double> = emptyMap()
    private var allCurrenciesMap: Map<String, Double> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_popular_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("API_TEST", "PopularRatesFragment запустился")

        val rvHorizontal = view.findViewById<RecyclerView>(R.id.rvHorizontal)
        val rvVertical = view.findViewById<RecyclerView>(R.id.rvVertical)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        rvHorizontal.adapter = horizontalAdapter
        rvVertical.adapter = verticalAdapter

        horizontalAdapter.onItemClick = { code, rate ->
            val bundle = Bundle().apply {
                putString("currency_code", code)
                putDouble("currency_rate", rate)
            }

            findNavController().navigate(
                R.id.rateDetailsFragment,
                bundle
            )
        }

        verticalAdapter.onItemClick = { code, rate ->
            val bundle = Bundle().apply {
                putString("currency_code", code)
                putDouble("currency_rate", rate)
            }

            findNavController().navigate(
                R.id.rateDetailsFragment,
                bundle
            )
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterVerticalList(newText.orEmpty())
                return true
            }
        })

        val repository = CurrencyRepository(
            NetworkModule.apiService,
            NetworkModule.currencyDao
        )

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val response = repository.fetchLatestRates("USD")

                Log.d("API_TEST", "HTTP = ${response.code()}")
                Log.d("API_TEST", "OK = ${response.isSuccessful}")

                if (response.isSuccessful) {

                    val rates = response.body()?.rates ?: emptyMap()

                    allCurrenciesMap = rates

                    repository.saveRatesToDatabase(rates)


                    val topCodes = setOf("USD", "EUR", "RUB", "CNY")

                    val horizontalRates = rates.filter {
                        it.key in topCodes
                    }

                    horizontalAdapter.submitRates(horizontalRates)

                    allRatesMap = rates.filter {
                        it.key !in topCodes
                    }

                    verticalAdapter.submitRates(allRatesMap)
                }

            } catch (e: Exception) {
                Log.e("API_TEST", "Ошибка сети", e)
            }
        }
    }

    private fun filterVerticalList(query: String) {

        if (query.isBlank()) {
            verticalAdapter.submitRates(allRatesMap)
            return
        }

        val filtered = allCurrenciesMap.filter { (code, _) ->

            code.contains(query, ignoreCase = true) ||
                    CurrencyNames.getName(code)
                        .contains(query, ignoreCase = true)
        }

        verticalAdapter.submitRates(filtered)
    }
}