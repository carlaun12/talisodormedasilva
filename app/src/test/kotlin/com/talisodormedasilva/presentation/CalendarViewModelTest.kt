package com.talisodormedasilva.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStore
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import com.talisodormedasilva.domain.FinancialItemRepository
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {
    @get:Rule val main = MainDispatcherRule()

    private fun repository(items: List<FinancialItem> = emptyList()) = object : FinancialItemRepository {
        override fun observeMonth(month: YearMonth) = flowOf(items)
    }

    private fun withModel(repository: FinancialItemRepository, date: LocalDate, block: (CalendarViewModel) -> Unit) {
        val model = CalendarViewModel(repository, SavedStateHandle(), date)
        val store = ViewModelStore()
        store.put("calendar", model)
        try { block(model) } finally { store.clear() }
    }

    @Test fun persistedItemsPopulateStateAndDateSelection() = runTest(main.dispatcher) {
        val date = LocalDate.of(2024, 2, 29)
        val item = FinancialItem(1, date, "Income", FinancialDirection.INFLOW)
        withModel(repository(listOf(item)), date.minusDays(1)) { model ->
            testScheduler.runCurrent()
            assertFalse(model.state.value.isLoading)
            assertTrue(model.state.value.selectedItems.isEmpty())
            model.selectDate(date)
            assertEquals(listOf(item), model.state.value.selectedItems)
            model.selectDate(date.plusDays(1))
            assertEquals(date, model.state.value.selectedDate)
        }
    }

    @Test fun monthNavigationClampsDayAndCrossesYearBoundary() = runTest(main.dispatcher) {
        withModel(repository(), LocalDate.of(2024, 1, 31)) { model ->
            testScheduler.runCurrent()
            model.nextMonth()
            assertEquals(LocalDate.of(2024, 2, 29), model.state.value.selectedDate)
            model.previousMonth()
            model.previousMonth()
            assertEquals(YearMonth.of(2023, 12), model.state.value.month)
        }
    }

    @Test fun readFailureIsVisibleAndIsNotAnEmptySuccessfulResult() = runTest(main.dispatcher) {
        val failing = object : FinancialItemRepository {
            override fun observeMonth(month: YearMonth): Flow<List<FinancialItem>> = flow { error("Read failed") }
        }
        withModel(failing, LocalDate.of(2026, 10, 4)) { model ->
            testScheduler.runCurrent()
            assertTrue(model.state.value.hasLoadError)
            assertFalse(model.state.value.isLoading)
            assertEquals("Read failed", model.state.value.loadError?.message)
        }
    }

    @Test fun restoresDateFromSavedState() = runTest(main.dispatcher) {
        val saved = SavedStateHandle(mapOf("selectedDate" to "2024-02-29"))
        val model = CalendarViewModel(repository(), saved, LocalDate.of(2026, 10, 4))
        val store = ViewModelStore().apply { put("calendar", model) }
        try {
            assertEquals(LocalDate.of(2024, 2, 29), model.state.value.selectedDate)
            model.selectDate(LocalDate.of(2024, 2, 28))
            assertEquals("2024-02-28", saved.get<String>("selectedDate"))
        } finally { store.clear() }
    }

    @Test fun navigationCancelsPreviousMonthObservation() = runTest(main.dispatcher) {
        val months = mutableMapOf<YearMonth, MutableSharedFlow<List<FinancialItem>>>()
        val source = object : FinancialItemRepository {
            override fun observeMonth(month: YearMonth) = months.getOrPut(month) { MutableSharedFlow() }
        }
        withModel(source, LocalDate.of(2026, 10, 4)) { model ->
            testScheduler.runCurrent()
            val october = months.getValue(YearMonth.of(2026, 10))
            assertEquals(1, october.subscriptionCount.value)
            model.nextMonth()
            testScheduler.runCurrent()
            assertEquals(0, october.subscriptionCount.value)
            assertEquals(1, months.getValue(YearMonth.of(2026, 11)).subscriptionCount.value)
            assertTrue(model.state.value.isLoading)
            assertTrue(model.state.value.itemsByDate.isEmpty())
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) { Dispatchers.setMain(dispatcher) }
    override fun finished(description: Description) { Dispatchers.resetMain() }
}
