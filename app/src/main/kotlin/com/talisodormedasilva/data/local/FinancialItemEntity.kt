package com.talisodormedasilva.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import java.time.LocalDate

@Entity(tableName = "financial_items", indices = [Index("epochDay")])
internal data class FinancialItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val epochDay: Long,
    val label: String,
    val direction: String,
)

internal fun FinancialItemEntity.toDomain() = FinancialItem(
    id = id,
    date = LocalDate.ofEpochDay(epochDay),
    label = label,
    direction = FinancialDirection.valueOf(direction),
)

internal fun FinancialItem.toEntity() = FinancialItemEntity(
    id = id,
    epochDay = date.toEpochDay(),
    label = label,
    direction = direction.name,
)
