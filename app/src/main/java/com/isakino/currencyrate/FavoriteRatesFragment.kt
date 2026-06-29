package com.isakino.currencyrate

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isakino.currencyrate.data.networ.CurrencyAdapter
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch

class FavoriteRatesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = CurrencyAdapter()

    private lateinit var repository: CurrencyRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvFavorites)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        repository = CurrencyRepository(
            NetworkModule.apiService,
            NetworkModule.currencyDao
        )

        loadFavorites()

        adapter.onItemLongClick = { code, rate ->

            AlertDialog.Builder(requireContext())
                .setTitle("Удаление")
                .setMessage("Удалить $code из избранного?")
                .setPositiveButton("Удалить") { _, _ ->

                    lifecycleScope.launch {

                        repository.removeFromFavorites(code, rate)

                        loadFavorites()
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun loadFavorites() {

        lifecycleScope.launch {

            val list = repository.getFavoriteCurrencies()

            val map = mutableMapOf<String, Double>()

            list.forEach {
                map[it.code] = it.rate
            }

            adapter.submitRates(map)
        }
    }
}