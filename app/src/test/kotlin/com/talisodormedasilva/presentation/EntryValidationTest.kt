package com.talisodormedasilva.presentation

import org.junit.Assert.*
import org.junit.Test

class EntryValidationTest {
    @Test fun decimalInputPreservesExactCents() {
        assertEquals(124099L, parseAmountCents("1240,99"))
        assertEquals(1L, parseAmountCents("0.01"))
        assertEquals(100L, parseAmountCents(" 1 "))
        assertEquals(Long.MAX_VALUE, parseAmountCents("92233720368547758.07"))
    }

    @Test fun rejectsNonPositiveOverPrecisionGroupingAndOverflow() {
        listOf("", "0", "-1", "0,001", "1.240,99", "1e3", "NaN", "92233720368547758.08").forEach {
            assertNull(it, parseAmountCents(it))
        }
    }
}
