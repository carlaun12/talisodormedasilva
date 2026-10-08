package com.talisodormedasilva.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talisodormedasilva.domain.FinancialItemRepository
import com.talisodormedasilva.domain.groupItemsByDate
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val repository: FinancialItemRepository,
    private val savedState: SavedStateHandle,
    initialDate: LocalDate = LocalDate.now(),
) : ViewModel() {
    private val restoredDate = savedState.get<String>("selectedDate")?.let(LocalDate::parse) ?: initialDate
    private val mutableState = MutableStateFlow(CalendarState(YearMonth.from(restoredDate), restoredDate))
    val state = mutableState.asStateFlow()
    private var observation: Job? = null

    init { observeMonth() }

    fun selectDate(date: LocalDate) {
        if (YearMonth.from(date) != state.value.month) return
        savedState["selectedDate"] = date.toString()
        mutableState.update { it.copy(selectedDate = date) }
    }

    fun previousMonth() = moveMonth(-1)
    fun nextMonth() = moveMonth(1)

    private fun moveMonth(offset: Long) {
        val month = state.value.month.plusMonths(offset)
        val date = month.atDay(minOf(state.value.selectedDate.dayOfMonth, month.lengthOfMonth()))
        savedState["selectedDate"] = date.toString()
        mutableState.value = CalendarState(month, date)
        observeMonth()
    }

    private fun observeMonth() {
        observation?.cancel()
        val month = state.value.month
        observation = viewModelScope.launch {
            repository.observeMonth(month)
                .catch { failure -> mutableState.update { it.copy(isLoading = false, loadError = failure) } }
                .collect { items ->
                    mutableState.update {
                        it.copy(itemsByDate = groupItemsByDate(items, month), isLoading = false, loadError = null)
                    }
                }
        }
    }
}
