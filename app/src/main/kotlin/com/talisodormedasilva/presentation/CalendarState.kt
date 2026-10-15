package com.talisodormedasilva.presentation

import com.talisodormedasilva.domain.FinancialItem
import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val month: YearMonth,
    val selectedDate: LocalDate,
    val itemsByDate: Map<LocalDate, List<FinancialItem>> = emptyMap(),
    val isLoading: Boolean = true,
    val loadError: Throwable? = null,
) {
    val hasLoadError: Boolean get() = loadError != null
    val selectedItems: List<FinancialItem> get() = itemsByDate[selectedDate].orEmpty()
}

// Monday-first slots with padding only, not another calendar/event domain.
fun monthSlots(month: YearMonth): List<LocalDate?> {
    val leading = month.atDay(1).dayOfWeek.value - 1
    val dates = (1..month.lengthOfMonth()).map(month::atDay)
    val trailing = (7 - (leading + dates.size) % 7) % 7
    return List(leading) { null } + dates + List(trailing) { null }
}
