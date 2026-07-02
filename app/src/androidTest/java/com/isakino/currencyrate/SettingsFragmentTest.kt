package com.isakino.currencyrate

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    @Test
    fun settingsFragment_shouldDisplayAllControls() {

        launchFragmentInContainer<SettingsFragment>(
            themeResId = androidx.appcompat.R.style.Theme_AppCompat
        )

        onView(withId(R.id.radioGroupTheme))
            .check(matches(isDisplayed()))

        onView(withId(R.id.radioGroupStartScreen))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbSystem))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbLight))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbDark))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbHome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbFavorite))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbHistory))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rbNotifications))
            .check(matches(isDisplayed()))
    }
}