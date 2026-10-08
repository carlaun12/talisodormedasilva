package com.talisodormedasilva.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FinancialItemDao {
    @Query("SELECT * FROM financial_items WHERE epochDay >= :startInclusive AND epochDay < :endExclusive ORDER BY epochDay, id")
    fun observeRange(startInclusive: Long, endExclusive: Long): Flow<List<FinancialItemEntity>>

    // Foundation tests can persist fixtures; no production creation workflow is exposed.
    @Insert
    suspend fun insert(item: FinancialItemEntity): Long
}
