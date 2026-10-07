package com.talisodormedasilva.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "en-rUS")
@LooperMode(LooperMode.Mode.PAUSED)
class CalendarScreenTest {
    @get:Rule val compose = createComposeRule()
    private val date = LocalDate.of(2024, 2, 15)

    @Test fun datedFixtureShowsBothDirections() {
        val fixture = listOf(
            FinancialItem(1, date, "Income fixture", FinancialDirection.INFLOW),
            FinancialItem(2, date, "Card obligation fixture", FinancialDirection.OUTFLOW),
        )
        show(CalendarState(YearMonth.from(date), date, mapOf(date to fixture), isLoading = false))
        compose.onNodeWithTag("day-$date").assertIsSelected()
        compose.onNodeWithTag("month-summary").assertExists()
        compose.onAllNodesWithText("1 items").assertCountEquals(2)
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Expected inflow"))
        compose.onNodeWithText("Expected inflow").assertExists()
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Obligation / expected outflow"))
        compose.onNodeWithText("Obligation / expected outflow").assertExists()
    }

    @Test fun emptyDatabaseHasExplicitEmptyState() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false))
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Nothing on this date"))
        compose.onNodeWithText("Nothing on this date").assertExists()
        compose.onNodeWithText("No financial items on this date.").assertExists()
    }

    @Test fun readFailureIsVisible() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false, loadError = IllegalStateException("Fixture read failure")))
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Local financial items could not be loaded."))
        compose.onNodeWithText("Local financial items could not be loaded.").assertExists()
    }

    @Test fun navigationAndDateSelectionEmitEvents() {
        var previous = 0
        var next = 0
        var selected: LocalDate? = null
        compose.setContent {
            MaterialTheme {
                CalendarScreen(CalendarState(YearMonth.from(date), date, isLoading = false),
                    { previous++ }, { next++ }, { selected = it })
            }
        }
        compose.onNodeWithTag("previous-month").performClick()
        compose.onNodeWithTag("next-month").performClick()
        compose.onNodeWithTag("day-2024-02-16").performScrollTo().performClick()
        assertEquals(1, previous)
        assertEquals(1, next)
        assertEquals(LocalDate.of(2024, 2, 16), selected)
    }

    private fun show(state: CalendarState) {
        compose.setContent { MaterialTheme { CalendarScreen(state, {}, {}, {}) } }
    }
}
