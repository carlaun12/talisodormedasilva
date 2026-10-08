package com.talisodormedasilva.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FinancialItemEntity::class], version = 1, exportSchema = true)
internal abstract class FinancialDatabase : RoomDatabase() {
    abstract fun financialItems(): FinancialItemDao

    companion object {
        fun open(context: Context): FinancialDatabase =
            Room.databaseBuilder(context.applicationContext, FinancialDatabase::class.java, "financial-calendar.db")
                .build()
    }
}
