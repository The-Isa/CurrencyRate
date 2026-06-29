package com.isakino.currencyrate

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Применяем тему
        SettingsManager(this).applyTheme()

        super.onCreate(savedInstanceState)

        // Инициализация Room
        com.isakino.currencyrate.data.networ.NetworkModule
            .initializeDatabase(applicationContext)

        setContentView(R.layout.activity_main)

        requestNotificationPermission()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNavigation =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)

        NavigationUI.setupWithNavController(
            bottomNavigation,
            navController
        )

        // Открываем выбранный стартовый экран
        if (savedInstanceState == null) {

            when (SettingsManager(this).getStartScreen()) {

                SettingsManager.SCREEN_HOME -> {
                    navController.navigate(R.id.popularRatesFragment)
                }

                SettingsManager.SCREEN_FAVORITES -> {
                    navController.navigate(R.id.favoriteRatesFragment)
                }

                SettingsManager.SCREEN_HISTORY -> {
                    navController.navigate(R.id.rateHistoryFragment)
                }

                SettingsManager.SCREEN_NOTIFICATIONS -> {
                    navController.navigate(R.id.notificationFragment)
                }
            }
        }
    }

    private fun requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}