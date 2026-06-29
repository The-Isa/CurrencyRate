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
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvNotifications)

        adapter = NotificationAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val repository = CurrencyRepository(
            NetworkModule.apiService,
            NetworkModule.currencyDao
        )

        loadNotifications(repository)

        adapter.onDeleteClick = { notification ->

            lifecycleScope.launch {

                // Отменяем запланированное уведомление
                NotificationScheduler(requireContext())
                    .cancelNotification(notification.currencyCode.hashCode())

                // Удаляем из базы
                repository.removeNotification(notification)

                // Обновляем список
                loadNotifications(repository)
            }
        }
    }

    private fun loadNotifications(repository: CurrencyRepository) {

        lifecycleScope.launch {

            val notifications = repository.getNotifications()

            adapter.submitList(notifications)
        }
    }
}