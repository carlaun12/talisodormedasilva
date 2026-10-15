package com.talisodormedasilva.domain

import java.time.LocalDate
import java.time.YearMonth

enum class FinancialDirection { INFLOW, OUTFLOW }

data class FinancialItem(
    val id: Long,
    val date: LocalDate,
    val label: String,
    val direction: FinancialDirection,
)

fun groupItemsByDate(items: List<FinancialItem>, month: YearMonth): Map<LocalDate, List<FinancialItem>> =
    items.filter { YearMonth.from(it.date) == month }
        .sortedWith(compareBy({ it.date }, { it.id }))
        .groupBy { it.date }
