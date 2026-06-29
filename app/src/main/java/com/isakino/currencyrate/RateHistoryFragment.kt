package com.isakino.currencyrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.HistoryAdapter
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch

class RateHistoryFragment : Fragment() {

    private val historyAdapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Привязываем RecyclerView истории из XML к коду
        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        rvHistory.adapter = historyAdapter

        // 2. Создаем репозиторий, передавая сеть и DAO
        val repository = CurrencyRepository(NetworkModule.apiService, NetworkModule.currencyDao)

        // 3. Запускаем корутину, забираем данные из Room и отправляем в адаптер
        viewLifecycleOwner.lifecycleScope.launch {
            val historyData = repository.getHistoryList()
            historyAdapter.submitList(historyData)
        }
    }
}
