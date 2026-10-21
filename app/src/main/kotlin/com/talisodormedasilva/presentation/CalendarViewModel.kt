package com.talisodormedasilva.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talisodormedasilva.domain.FinancialItem
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItemRepository
import com.talisodormedasilva.domain.groupItemsByDate
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.CancellationException
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
    private val mutableState = MutableStateFlow(CalendarState(
        YearMonth.from(restoredDate),
        restoredDate,
        showAddAction = savedState.get<Boolean>("showAddAction") ?: false,
    ))
    val state = mutableState.asStateFlow()
    private var observation: Job? = null

    init { observeMonth() }

    fun selectDate(date: LocalDate) {
        if (state.value.isSaving || YearMonth.from(date) != state.value.month) return
        savedState["selectedDate"] = date.toString()
        savedState["showAddAction"] = true
        mutableState.update { it.copy(selectedDate = date, showAddAction = true) }
    }

    fun openAddItem() { if (!state.value.isSaving) mutableState.update { it.copy(isAdding = true, saveError = false) } }

    fun closeAddItem() {
        if (!state.value.isSaving) mutableState.update { it.copy(isAdding = false, saveError = false) }
    }

    fun addItem(label: String, direction: FinancialDirection, amountCents: Long, date: LocalDate) {
        if (state.value.isSaving || label.trim().isEmpty() || label.trim().length > 90 || amountCents <= 0) return
        mutableState.update { it.copy(isSaving = true, saveError = false) }
        viewModelScope.launch {
            try {
                repository.add(FinancialItem(0, date, label.trim(), direction, amountCents))
                savedState["selectedDate"] = date.toString()
                val monthChanged = YearMonth.from(date) != state.value.month
                if (monthChanged) savedState["showAddAction"] = false
                mutableState.update {
                    if (monthChanged) CalendarState(YearMonth.from(date), date)
                    else it.copy(selectedDate = date, isAdding = false, isSaving = false, saveError = false)
                }
                if (monthChanged) observeMonth()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.update { it.copy(isSaving = false, saveError = true) }
            }
        }
    }

    fun previousMonth() = moveMonth(-1)
    fun nextMonth() = moveMonth(1)

    private fun moveMonth(offset: Long) {
        if (state.value.isSaving) return
        val month = state.value.month.plusMonths(offset)
        val date = month.atDay(minOf(state.value.selectedDate.dayOfMonth, month.lengthOfMonth()))
        savedState["selectedDate"] = date.toString()
        savedState["showAddAction"] = false
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
