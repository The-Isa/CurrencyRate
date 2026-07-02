package com.isakino.currencyrate

import android.Manifest
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("NOTIFICATION", "Receiver вызван")
        Toast.makeText(
            context,
            "Receiver вызван!",
            Toast.LENGTH_LONG
        ).show()

        val title = intent.getStringExtra("title") ?: "Напоминание"
        val message = intent.getStringExtra("message") ?: "Проверьте курс валют"

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("NOTIFICATION", "Нет разрешения POST_NOTIFICATIONS")
            return
        }

        val notification = NotificationCompat.Builder(
            context,
            "currency_channel"
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        val notificationId = intent.action.hashCode()

        manager.notify(
            notificationId,
            notification
        )

        Log.d("NOTIFICATION", "Уведомление отправлено")
    }
}