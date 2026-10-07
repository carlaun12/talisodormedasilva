package com.talisodormedasilva.platform

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.ViewModelProvider
import com.talisodormedasilva.presentation.CalendarViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "en-rUS")
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun realApplicationWiringLoadsEmptyRoomDatabaseIntoCalendar() {
        compose.onNodeWithTag("month-title").assertExists()
        compose.onNodeWithTag("month-summary").assertExists()
        compose.waitUntil(timeoutMillis = 10_000) {
            !ViewModelProvider(compose.activity)[CalendarViewModel::class.java].state.value.isLoading
        }
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("No financial items on this date."))
        compose.onNodeWithText("No financial items on this date.").assertExists()
    }
}
