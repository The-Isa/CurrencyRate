package com.isakino.currencyrate

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SettingsManager(context: Context) {

    companion object {

        private const val PREF_NAME = "app_settings"

        private const val KEY_THEME = "theme"
        private const val KEY_START_SCREEN = "start_screen"

        // Тема
        const val THEME_SYSTEM = 0
        const val THEME_LIGHT = 1
        const val THEME_DARK = 2

        // Стартовый экран
        const val SCREEN_HOME = 0
        const val SCREEN_FAVORITES = 1
        const val SCREEN_HISTORY = 2
        const val SCREEN_NOTIFICATIONS = 3
    }

    private val preferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ==========================
    // ТЕМА
    // ==========================

    fun saveTheme(theme: Int) {
        preferences.edit()
            .putInt(KEY_THEME, theme)
            .apply()
    }

    fun getTheme(): Int {
        return preferences.getInt(KEY_THEME, THEME_SYSTEM)
    }

    fun applyTheme() {

        when (getTheme()) {

            THEME_LIGHT ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

            THEME_DARK ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )

            else ->
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
        }
    }

    // ==========================
    // СТАРТОВЫЙ ЭКРАН
    // ==========================

    fun saveStartScreen(screen: Int) {
        preferences.edit()
            .putInt(KEY_START_SCREEN, screen)
            .apply()
    }

    fun getStartScreen(): Int {
        return preferences.getInt(
            KEY_START_SCREEN,
            SCREEN_HOME
        )
    }
}