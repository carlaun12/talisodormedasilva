package com.talisodormedasilva.presentation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.hasTestTag
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

    @Test fun addButtonAppearsOnlyAfterTappingDay() {
        val visibleState = mutableStateOf(CalendarState(YearMonth.from(date), date, isLoading = false))
        var openCount = 0
        compose.setContent {
            CalendarTheme {
                CalendarScreen(
                    state = visibleState.value,
                    onPreviousMonth = {},
                    onNextMonth = {},
                    onSelectDate = { selected ->
                        visibleState.value = visibleState.value.copy(selectedDate = selected, showAddAction = true)
                    },
                    onOpenAddItem = { openCount++ },
                )
            }
        }
        compose.onNodeWithTag("add-item").assertDoesNotExist()
        compose.onNodeWithTag("day-$date").performScrollTo().performClick()
        compose.onNodeWithTag("calendar").performScrollToNode(hasTestTag("add-item"))
        compose.onNodeWithTag("add-item").assertExists().performClick()
        assertEquals(1, openCount)
    }

    @Test fun emptyDayDoesNotRenderDayDetails() {
        show(CalendarState(YearMonth.from(date), date, isLoading = false))
        compose.onNodeWithTag("day-$date").assertIsSelected()
        compose.onNodeWithText("Nenhum lançamento neste dia").assertDoesNotExist()
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
