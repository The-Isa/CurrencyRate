package com.isakino.currencyrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.CurrencyPagingAdapter
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteRatesFragment : Fragment() {

    // Создаем экземпляр нашего адаптера для пагинации
    private val pagingAdapter = CurrencyPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Подключаем ваш файл разметки фрагмента (проверьте имя, если отличается)
        return inflater.inflate(R.layout.fragment_favorite_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Инициализируем RecyclerView внутри разметки фрагмента избранного
        // Обратите внимание: в XML fragment_favorite_rates должен быть RecyclerView с id recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pagingAdapter

        // 2. Создаем репозиторий, передавая apiService и наш готовый currencyDao
        val repository = CurrencyRepository(NetworkModule.apiService, NetworkModule.currencyDao)

        // 3. Подписываемся на поток пагинации (Flow) и отправляем страницы в адаптер
        viewLifecycleOwner.lifecycleScope.launch {
            repository.getPagedFavoriteCurrencies().collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
}
