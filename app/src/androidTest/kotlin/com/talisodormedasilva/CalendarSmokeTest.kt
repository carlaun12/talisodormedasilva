package com.talisodormedasilva

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
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

    @Test fun cleanInstallShowsCalendarAndEmptyLocalDatabase() {
        compose.onNodeWithText("Financial calendar").assertExists()
        compose.waitUntil(timeoutMillis = 10_000) {
            !ViewModelProvider(compose.activity)[CalendarViewModel::class.java].state.value.isLoading
        }
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("No financial items on this date."))
        compose.onNodeWithText("No financial items on this date.").assertExists()
    }
}
