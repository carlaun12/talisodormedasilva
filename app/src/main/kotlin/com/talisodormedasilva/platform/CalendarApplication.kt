package com.talisodormedasilva.platform

import android.app.Application
import com.talisodormedasilva.data.local.FinancialDatabase
import com.talisodormedasilva.data.local.RoomFinancialItemRepository
import com.talisodormedasilva.domain.FinancialItemRepository

class CalendarApplication : Application() {
    val repository: FinancialItemRepository by lazy {
        RoomFinancialItemRepository(FinancialDatabase.open(this).financialItems())
    }
}
