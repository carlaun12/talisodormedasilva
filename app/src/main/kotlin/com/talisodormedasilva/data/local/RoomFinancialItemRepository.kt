package com.talisodormedasilva.data.local

import com.talisodormedasilva.domain.FinancialItemRepository
import java.time.YearMonth
import kotlinx.coroutines.flow.map

internal class RoomFinancialItemRepository(private val dao: FinancialItemDao) : FinancialItemRepository {
    override fun observeMonth(month: YearMonth) = dao.observeRange(
        month.atDay(1).toEpochDay(),
        month.plusMonths(1).atDay(1).toEpochDay(),
    ).map { records -> records.map { it.toDomain() } }
}
