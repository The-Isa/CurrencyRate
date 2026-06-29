package com.isakino.currencyrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        // ==========================
        // ТЕМА
        // ==========================

        val themeGroup =
            view.findViewById<RadioGroup>(R.id.radioGroupTheme)

        val rbSystem =
            view.findViewById<RadioButton>(R.id.rbSystem)

        val rbLight =
            view.findViewById<RadioButton>(R.id.rbLight)

        val rbDark =
            view.findViewById<RadioButton>(R.id.rbDark)

        when (settingsManager.getTheme()) {

            SettingsManager.THEME_SYSTEM ->
                rbSystem.isChecked = true

            SettingsManager.THEME_LIGHT ->
                rbLight.isChecked = true

            SettingsManager.THEME_DARK ->
                rbDark.isChecked = true
        }

        themeGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.rbSystem ->
                    settingsManager.saveTheme(SettingsManager.THEME_SYSTEM)

                R.id.rbLight ->
                    settingsManager.saveTheme(SettingsManager.THEME_LIGHT)

                R.id.rbDark ->
                    settingsManager.saveTheme(SettingsManager.THEME_DARK)
            }

            settingsManager.applyTheme()

            requireActivity().recreate()
        }

        // ==========================
        // СТАРТОВЫЙ ЭКРАН
        // ==========================

        val startGroup =
            view.findViewById<RadioGroup>(R.id.radioGroupStartScreen)

        val rbHome =
            view.findViewById<RadioButton>(R.id.rbHome)

        val rbFavorite =
            view.findViewById<RadioButton>(R.id.rbFavorite)

        val rbHistory =
            view.findViewById<RadioButton>(R.id.rbHistory)

        val rbNotifications =
            view.findViewById<RadioButton>(R.id.rbNotifications)

        when (settingsManager.getStartScreen()) {

            SettingsManager.SCREEN_HOME ->
                rbHome.isChecked = true

            SettingsManager.SCREEN_FAVORITES ->
                rbFavorite.isChecked = true

            SettingsManager.SCREEN_HISTORY ->
                rbHistory.isChecked = true

            SettingsManager.SCREEN_NOTIFICATIONS ->
                rbNotifications.isChecked = true
        }

        startGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.rbHome ->
                    settingsManager.saveStartScreen(
                        SettingsManager.SCREEN_HOME
                    )

                R.id.rbFavorite ->
                    settingsManager.saveStartScreen(
                        SettingsManager.SCREEN_FAVORITES
                    )

                R.id.rbHistory ->
                    settingsManager.saveStartScreen(
                        SettingsManager.SCREEN_HISTORY
                    )

                R.id.rbNotifications ->
                    settingsManager.saveStartScreen(
                        SettingsManager.SCREEN_NOTIFICATIONS
                    )
            }
        }
    }
}