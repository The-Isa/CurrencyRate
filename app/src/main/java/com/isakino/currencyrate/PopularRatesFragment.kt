package com.isakino.currencyrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.CurrencyAdapter
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch

class PopularRatesFragment : Fragment() {

    // Создаем два адаптера под две подборки валют из ТЗ ментора
    private val horizontalAdapter = CurrencyAdapter()
    private val verticalAdapter = CurrencyAdapter()

    // Храним исходный список всех курсов из сети для фильтрации в поиске
    private var allRatesMap: Map<String, Double> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Подключаем ваш файл разметки фрагмента
        return inflater.inflate(R.layout.fragment_popular_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Привязываем списки RecyclerView из XML к коду
        val rvHorizontal = view.findViewById<RecyclerView>(R.id.rvHorizontal)
        val rvVertical = view.findViewById<RecyclerView>(R.id.rvVertical)

        rvHorizontal.adapter = horizontalAdapter
        rvVertical.adapter = verticalAdapter

        // 2. Привязываем поисковое поле SearchView из XML к коду
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        // 3. Настраиваем логику работы поиска валют в реальном времени
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterVerticalList(newText.orEmpty())
                return true
            }
        })

        // 4. Запускаем корутину и скачиваем актуальные данные из интернета
        val repository = CurrencyRepository(NetworkModule.apiService, NetworkModule.currencyDao)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = repository.fetchLatestRates("USD")
                if (response.isSuccessful) {
                    val rates = response.body()?.rates ?: emptyMap()

                    // Горизонтальная подборка: берем основные топ-валюты
                    val topCodes = setOf("USD", "EUR", "RUB", "CNY")
                    val horizontalRates = rates.filter { it.key in topCodes }
                    horizontalAdapter.submitRates(horizontalRates)

                    // Вертикальная подборка: все остальные валюты
                    allRatesMap = rates.filter { it.key !in topCodes }
                    verticalAdapter.submitRates(allRatesMap)
                }
            } catch (e: Exception) {
                // Ошибки сети перехватываются здесь
            }
        }
    }

    // Метод для фильтрации основного вертикального списка по коду валюты
    private fun filterVerticalList(query: String) {
        if (query.isEmpty()) {
            verticalAdapter.submitRates(allRatesMap)
        } else {
            val filtered = allRatesMap.filter { it.key.contains(query, ignoreCase = true) }
            verticalAdapter.submitRates(filtered)
        }
    }
}
