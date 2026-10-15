package com.talisodormedasilva.presentation

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.*
import org.junit.Test

class CalendarStateTest {
    @Test fun leapFebruaryHas29DatesAndWholeWeeks() {
        val month = YearMonth.of(2024, 2)
        val slots = monthSlots(month)
        assertEquals(0, slots.size % 7)
        assertEquals(3, slots.takeWhile { it == null }.size)
        assertEquals((1..29).map(month::atDay), slots.filterNotNull())
    }

    @Test fun mondayStartNeedsNoLeadingPadding() {
        assertEquals(LocalDate.of(2024, 1, 1), monthSlots(YearMonth.of(2024, 1)).first())
    }

    @Test fun sixWeekMonthKeepsFinalDate() {
        val slots = monthSlots(YearMonth.of(2026, 3))
        assertEquals(42, slots.size)
        assertEquals(LocalDate.of(2026, 3, 31), slots.filterNotNull().last())
    }
}
