package com.talisodormedasilva

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.talisodormedasilva.platform.MainActivity
import com.talisodormedasilva.presentation.CalendarViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarSmokeTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun cleanInstallShowsCalendarAfterLocalDatabaseLoads() {
        compose.onNodeWithTag("month-title").assertExists()
        val model = ViewModelProvider(compose.activity)[CalendarViewModel::class.java]
        compose.waitUntil(timeoutMillis = 10_000) { !model.state.value.isLoading }
        compose.onNodeWithTag("calendar").assertExists()
        compose.onNodeWithTag("day-" + model.state.value.selectedDate).assertExists()
    }
}
