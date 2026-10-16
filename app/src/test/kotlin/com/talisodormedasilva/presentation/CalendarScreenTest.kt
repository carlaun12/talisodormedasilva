package com.talisodormedasilva.presentation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
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
@Config(sdk = [35], qualifiers = "en-rUS")
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
        compose.onNodeWithTag("total-incoming").assertExists()
        compose.onNodeWithTag("total-due").assertExists()
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Income fixture"))
        compose.onNodeWithText("Income fixture").assertExists()
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Card obligation fixture"))
        compose.onNodeWithText("Card obligation fixture").assertExists()
    }

    @Test fun emptyDatabaseHasExplicitEmptyState() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false))
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Nenhum lançamento neste dia"))
        compose.onNodeWithText("Nenhum lançamento neste dia").assertExists()
    }

    @Test fun readFailureIsVisible() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false, loadError = IllegalStateException("Fixture read failure")))
        compose.onNodeWithTag("calendar").performScrollToNode(hasText("Não foi possível carregar os itens financeiros locais."))
        compose.onNodeWithText("Não foi possível carregar os itens financeiros locais.").assertExists()
    }

    @Test fun navigationAndDateSelectionEmitEvents() {
        var previous = 0
        var next = 0
        var selected: LocalDate? = null
        compose.setContent {
            CalendarTheme {
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

    @Test fun summaryShowsMonthlyTotalsIncludingOtherDates() {
        val fixture = listOf(
            FinancialItem(1, date, "Conta", FinancialDirection.OUTFLOW, 124099),
            FinancialItem(2, date, "Cliente", FinancialDirection.INFLOW, 168000),
        )
        val other = date.plusDays(1)
        show(CalendarState(YearMonth.from(date), date, mapOf(date to fixture,
            other to listOf(FinancialItem(3, other, "Projeto", FinancialDirection.INFLOW, 200000))), isLoading = false))
        compose.onNodeWithTag("total-due").assertTextEquals(money(124099))
        compose.onNodeWithTag("total-incoming").assertTextEquals(money(368000))
    }

    @Test fun formRejectsInvalidAmountAndDateThenSubmitsValidEntry() {
        var saved: FinancialItem? = null
        compose.setContent {
            CalendarTheme {
                CalendarScreen(CalendarState(YearMonth.from(date), date, isLoading = false, isAdding = true), {}, {}, {},
                    onSaveItem = { label, direction, cents, selected -> saved = FinancialItem(0, selected, label, direction, cents) })
            }
        }
        compose.onNodeWithTag("entry-description").performTextInput("Aluguel")
        compose.onNodeWithTag("entry-amount").performTextInput("0")
        compose.onNodeWithTag("entry-date").performTextClearance()
        compose.onNodeWithTag("entry-date").performTextInput("31/02/2024")
        compose.onNodeWithTag("save-item").performScrollTo().performClick()
        assertEquals(null, saved)
        compose.onNodeWithTag("entry-amount").performScrollTo().performTextClearance()
        compose.onNodeWithTag("entry-amount").performTextInput("1240,99")
        compose.onNodeWithTag("entry-date").performScrollTo().performTextClearance()
        compose.onNodeWithTag("entry-date").performTextInput("29/02/2024")
        compose.onNodeWithTag("save-item").performScrollTo().performClick()
        assertEquals(FinancialItem(0, LocalDate.of(2024, 2, 29), "Aluguel", FinancialDirection.OUTFLOW, 124099), saved)
    }

    private fun show(state: CalendarState) {
        compose.setContent { CalendarTheme { CalendarScreen(state, {}, {}, {}) } }
    }
}
