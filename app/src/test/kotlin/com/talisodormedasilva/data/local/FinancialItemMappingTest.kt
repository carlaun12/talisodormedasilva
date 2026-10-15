package com.talisodormedasilva.data.local

import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Test

class FinancialItemMappingTest {
    @Test fun roundTripPreservesDateLabelIdentityAndDirection() {
        for (direction in FinancialDirection.entries) {
            val item = FinancialItem(7, LocalDate.of(2024, 2, 29), "Card obligation / income", direction)
            assertEquals(item, item.toEntity().toDomain())
        }
    }

    @Test fun dateIsStoredAsDayWithoutTimezoneConversion() {
        val item = FinancialItem(1, LocalDate.of(1969, 12, 31), "Item", FinancialDirection.OUTFLOW)
        assertEquals(-1L, item.toEntity().epochDay)
        assertEquals(item, item.toEntity().toDomain())
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownPersistedDirectionIsNotSilentlyTreatedAsOutflow() {
        FinancialItemEntity(1, 0, "Invalid", "UNKNOWN").toDomain()
    }
}
