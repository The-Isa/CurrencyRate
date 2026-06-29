package com.isakino.currencyrate

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class NotificationScheduler(
    private val context: Context
) {

    /**
     * Планирование уведомления на конкретную дату и время
     */
    fun scheduleNotification(
        id: Int,
        title: String,
        message: String,
        triggerTime: Long
    ) {

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "currency_notification_$id"

            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Планируем уведомление на выбранную дату и время
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    /**
     * Отмена запланированного уведомления
     */
    fun cancelNotification(id: Int) {

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "currency_notification_$id"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}