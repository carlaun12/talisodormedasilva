package com.talisodormedasilva.domain

import java.time.YearMonth
import kotlinx.coroutines.flow.Flow

interface FinancialItemRepository {
    fun observeMonth(month: YearMonth): Flow<List<FinancialItem>>
}
