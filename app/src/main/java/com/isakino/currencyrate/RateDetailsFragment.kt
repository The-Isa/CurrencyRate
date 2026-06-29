package com.isakino.currencyrate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.isakino.currencyrate.data.networ.CurrencyRepository
import com.isakino.currencyrate.data.networ.NetworkModule
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RateDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rate_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val code = arguments?.getString("currency_code") ?: ""
        val rate = arguments?.getDouble("currency_rate") ?: 0.0

        val tvCode = view.findViewById<TextView>(R.id.tvCurrencyCode)
        val tvRate = view.findViewById<TextView>(R.id.tvCurrencyRate)
        val btnFavorite = view.findViewById<Button>(R.id.btnFavorite)

        tvCode.text = code
        tvRate.text = String.format("%.4f", rate)

        val repository = CurrencyRepository(
            NetworkModule.apiService,
            NetworkModule.currencyDao
        )

        // Добавляем в историю
        lifecycleScope.launch {
            repository.addToHistory(code, rate)
        }

        btnFavorite.setOnClickListener {

            val calendar = Calendar.getInstance()

            // Выбор даты
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->

                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    // Выбор времени
                    TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->

                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            val triggerTime = calendar.timeInMillis

                            lifecycleScope.launch {

                                // Добавляем в избранное
                                repository.addToFavorites(code, rate)

                                // Сохраняем уведомление
                                repository.addNotification(
                                    currencyCode = code,
                                    message = "Проверьте курс валюты $code",
                                    triggerTime = triggerTime
                                )

                                // Планируем уведомление
                                NotificationScheduler(requireContext())
                                    .scheduleNotification(
                                        id = code.hashCode(),
                                        title = "💰 Напоминание",
                                        message = "Проверьте курс валюты $code",
                                        triggerTime = triggerTime
                                    )

                                val formatter = SimpleDateFormat(
                                    "dd.MM.yyyy HH:mm",
                                    Locale.getDefault()
                                )

                                Toast.makeText(
                                    requireContext(),
                                    "Напоминание создано на\n${formatter.format(calendar.time)}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}