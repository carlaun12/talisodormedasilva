package com.talisodormedasilva.domain

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.*
import org.junit.Test

class FinancialItemsTest {
    @Test fun groupingKeepsBothDirectionsAndExcludesOtherMonths() {
        val date = LocalDate.of(2024, 2, 29)
        val inflow = FinancialItem(2, date, "Income", FinancialDirection.INFLOW)
        val outflow = FinancialItem(1, date, "Card obligation", FinancialDirection.OUTFLOW)
        val other = FinancialItem(3, date.plusDays(1), "Other", FinancialDirection.OUTFLOW)
        val grouped = groupItemsByDate(listOf(inflow, other, outflow), YearMonth.of(2024, 2))
        assertEquals(setOf(date), grouped.keys)
        assertEquals(listOf(outflow, inflow), grouped[date])
        assertEquals(listOf(FinancialDirection.OUTFLOW, FinancialDirection.INFLOW), grouped[date]!!.map { it.direction })
    }

    @Test fun emptyMonthHasNoInventedRecords() {
        assertTrue(groupItemsByDate(emptyList(), YearMonth.of(2026, 10)).isEmpty())
    }

    @Test fun groupingOrdersDatesAcrossLeapMonth() {
        val items = listOf(29, 1, 15).map {
            FinancialItem(it.toLong(), LocalDate.of(2024, 2, it), "Item", FinancialDirection.INFLOW)
        }
        assertEquals(listOf(1, 15, 29), groupItemsByDate(items, YearMonth.of(2024, 2)).keys.map { it.dayOfMonth })
    }
}
