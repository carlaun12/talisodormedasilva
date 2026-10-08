package com.talisodormedasilva.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class FinancialDatabaseTest {
    private lateinit var database: FinancialDatabase
    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Before fun setUp() {
        context.deleteDatabase("financial-calendar.db")
        database = Room.inMemoryDatabaseBuilder(context, FinancialDatabase::class.java).build()
    }
    @After fun tearDown() {
        database.close()
        context.deleteDatabase("financial-calendar.db")
    }

    @Test fun rangeQueryIncludesFirstAndLastDayAndExcludesAdjacentMonths() = runBlocking {
        val dao = database.financialItems()
        val dates = listOf("2024-01-31", "2024-02-01", "2024-02-29", "2024-03-01")
        for ((index, value) in dates.withIndex()) {
            dao.insert(FinancialItemEntity(epochDay = LocalDate.parse(value).toEpochDay(), label = value,
                direction = if (index == 1) "INFLOW" else "OUTFLOW"))
        }
        val items = RoomFinancialItemRepository(dao).observeMonth(YearMonth.of(2024, 2)).first()
        assertEquals(listOf("2024-02-01", "2024-02-29"), items.map { it.date.toString() })
        assertEquals(listOf(FinancialDirection.INFLOW, FinancialDirection.OUTFLOW), items.map { it.direction })
    }

    @Test fun insertInvalidatesObservedMonth() = runBlocking {
        val repository = RoomFinancialItemRepository(database.financialItems())
        val initialEmission = CompletableDeferred<Unit>()
        val updates = async {
            repository.observeMonth(YearMonth.of(2026, 10))
                .onEach { if (it.isEmpty()) initialEmission.complete(Unit) }
                .take(2).toList()
        }
        withTimeout(10_000) {
            initialEmission.await()
            val date = LocalDate.of(2026, 10, 4)
            database.financialItems().insert(FinancialItemEntity(epochDay = date.toEpochDay(), label = "Fixture", direction = "INFLOW"))
            val emissions = updates.await()
            assertTrue(emissions.first().isEmpty())
            assertEquals("Fixture", emissions.last().single().label)
        }
    }

    @Test fun productionDatabaseStartsEmptyAndSurvivesReopening() = runBlocking {
        database.close()
        database = FinancialDatabase.open(context)
        val month = YearMonth.of(2026, 10)
        assertTrue(RoomFinancialItemRepository(database.financialItems()).observeMonth(month).first().isEmpty())
        val item = FinancialItem(0, month.atDay(4), "Durable fixture", FinancialDirection.OUTFLOW)
        val id = database.financialItems().insert(item.toEntity())
        database.close()
        database = FinancialDatabase.open(context)
        assertEquals(item.copy(id = id), RoomFinancialItemRepository(database.financialItems()).observeMonth(month).first().single())
        assertEquals(1, database.openHelper.readableDatabase.version)
    }

    @Test fun missingMigrationFailsInsteadOfDestroyingData() = runBlocking {
        database.close()
        database = FinancialDatabase.open(context)
        database.openHelper.writableDatabase.execSQL("PRAGMA user_version = 2")
        database.close()
        database = FinancialDatabase.open(context)
        try {
            database.openHelper.writableDatabase
            fail("A missing migration must not reset the durable database")
        } catch (expected: IllegalStateException) {
            assertTrue(expected.message.orEmpty().contains("migration", ignoreCase = true))
        }
    }
}
