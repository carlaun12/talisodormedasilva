package com.talisodormedasilva.data.local

import com.talisodormedasilva.domain.FinancialItem
import com.talisodormedasilva.domain.FinancialItemRepository
import java.time.YearMonth
import kotlinx.coroutines.flow.map

internal class RoomFinancialItemRepository(private val dao: FinancialItemDao) : FinancialItemRepository {
    override suspend fun add(item: FinancialItem) {
        val normalizedLabel = item.label.trim()
        require(normalizedLabel.isNotEmpty() && normalizedLabel.length <= 90)
        require(item.amountCents != null && item.amountCents > 0)
        dao.insert(item.copy(id = 0, label = normalizedLabel).toEntity())
    }

    override fun observeMonth(month: YearMonth) = dao.observeRange(
        month.atDay(1).toEpochDay(),
        month.plusMonths(1).atDay(1).toEpochDay(),
    ).map { records -> records.map { it.toDomain() } }
}
