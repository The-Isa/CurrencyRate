package com.isakino.currencyrate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.isakino.currencyrate.R.id.bottom_navigation

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализируем базу данных Room, передавая контекст приложения
        com.isakino.currencyrate.data.networ.NetworkModule.initializeDatabase(applicationContext)

        // Находим контейнер для наших фрагментов-экранов
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Находим нижнее меню переключения
        val bottomNavigation = findViewById<BottomNavigationView>( /* id = */ bottom_navigation)

        // Связываем меню с навигацией — они будут переключаться!
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}
