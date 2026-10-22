package com.talisodormedasilva.presentation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
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

    @Test fun financialPanelsAreHiddenWhileDaySelectionRemains() {
        val fixture = listOf(
            FinancialItem(1, date, "Income fixture", FinancialDirection.INFLOW),
            FinancialItem(2, date, "Card obligation fixture", FinancialDirection.OUTFLOW),
        )
        show(CalendarState(YearMonth.from(date), date, mapOf(date to fixture), isLoading = false))
        compose.onNodeWithTag("day-$date").assertIsSelected()
        compose.onNodeWithTag("total-incoming").assertDoesNotExist()
        compose.onNodeWithTag("total-due").assertDoesNotExist()
        compose.onNodeWithText("Income fixture").assertDoesNotExist()
        compose.onNodeWithText("Card obligation fixture").assertDoesNotExist()
    }

    @Test fun monthHeadingIsVisibleAndRemovedControlsStayHidden() {
        val visibleState = mutableStateOf(CalendarState(YearMonth.from(date), date, isLoading = false))
        compose.setContent {
            CalendarTheme {
                CalendarScreen(
                    state = visibleState.value,
                    onPreviousMonth = {},
                    onNextMonth = {},
                    onSelectDate = { selected ->
                        visibleState.value = visibleState.value.copy(selectedDate = selected)
                    },
                )
            }
        }
        compose.onNodeWithTag("month-heading").assertTextEquals("Fevereiro")
        compose.onNodeWithTag("month-subtitle").assertTextEquals("Seu mês financeiro · 2024")
        compose.onNodeWithTag("previous-month").assertDoesNotExist()
        compose.onNodeWithTag("next-month").assertDoesNotExist()
        compose.onNodeWithTag("add-item").assertDoesNotExist()
        compose.onNodeWithTag("day-${date.plusDays(1)}").performClick()
        compose.onNodeWithTag("day-${date.plusDays(1)}").assertIsSelected()
        compose.onNodeWithTag("add-item").assertDoesNotExist()
    }

    @Test fun sixWeekMonthFitsWithoutScrolling() {
        val august = YearMonth.of(2026, 8)
        show(CalendarState(august, august.atDay(1), isLoading = false))
        assertEquals(42, monthSlots(august).size)
        compose.onNodeWithTag("month-heading").assertTextEquals("Agosto")
        compose.onNodeWithTag("month-grid").assertIsDisplayed()
        (1..6).forEach { compose.onNodeWithTag("week-$it").assertIsDisplayed() }
        compose.onNodeWithTag("week-label-6").assertTextEquals("Semana 6")
        compose.onNodeWithTag("day-2026-08-31").assertIsDisplayed()
    }

    @Test fun octoberIsSeparatedIntoFiveWeeklyCards() {
        val october = YearMonth.of(2026, 10)
        show(CalendarState(october, october.atDay(21), isLoading = false))
        assertEquals(35, monthSlots(october).size)
        (1..5).forEach { compose.onNodeWithTag("week-$it").assertIsDisplayed() }
        compose.onNodeWithTag("week-6").assertDoesNotExist()
        compose.onNodeWithTag("week-label-1").assertTextEquals("Semana 1")
        compose.onNodeWithTag("week-label-5").assertTextEquals("Semana 5")
        compose.onNodeWithTag("day-2026-10-21").assertIsSelected()
        compose.onNodeWithTag("day-2026-10-31").assertIsDisplayed()
        compose.onNodeWithTag("add-item").assertDoesNotExist()
    }

    @Test fun februaryWithFourWeeksHasFourCards() {
        val february = YearMonth.of(2021, 2)
        show(CalendarState(february, february.atDay(10), isLoading = false))
        assertEquals(28, monthSlots(february).size)
        (1..4).forEach { compose.onNodeWithTag("week-$it").assertIsDisplayed() }
        compose.onNodeWithTag("week-5").assertDoesNotExist()
        compose.onNodeWithTag("day-2021-02-28").assertIsDisplayed()
    }

    @Test fun emptyDayDoesNotRenderDayDetails() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false))
        compose.onNodeWithTag("day-$date").assertIsSelected()
        compose.onNodeWithText("Nenhum lançamento neste dia").assertDoesNotExist()
    }

    @Test fun readFailureIsVisible() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false, loadError = IllegalStateException("Fixture read failure")))
        compose.onNodeWithText("Não foi possível carregar os itens financeiros locais.").assertExists()
    }

    @Test fun swipingCalendarNavigatesMonthsAndTappingSelectsDate() {
        var previous = 0
        var next = 0
        var selected: LocalDate? = null
        compose.setContent {
            CalendarTheme {
                CalendarScreen(CalendarState(YearMonth.from(date), date, isLoading = false),
                    { previous++ }, { next++ }, { selected = it })
            }
        }
        compose.onNodeWithTag("month-grid").performTouchInput { swipeRight() }
        compose.onNodeWithTag("month-grid").performTouchInput { swipeLeft() }
        compose.onNodeWithTag("day-2024-02-16").performClick()
        assertEquals(1, previous)
        assertEquals(1, next)
        assertEquals(LocalDate.of(2024, 2, 16), selected)
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
