package com.talisodormedasilva.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FinancialItemEntity::class], version = 2, exportSchema = true)
internal abstract class FinancialDatabase : RoomDatabase() {
    abstract fun financialItems(): FinancialItemDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE financial_items ADD COLUMN amountCents INTEGER DEFAULT NULL")
            }
        }

        fun open(context: Context): FinancialDatabase =
            Room.databaseBuilder(context.applicationContext, FinancialDatabase::class.java, "financial-calendar.db")
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
