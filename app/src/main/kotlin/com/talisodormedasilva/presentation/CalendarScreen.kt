package com.talisodormedasilva.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.dp
import com.talisodormedasilva.R
import com.talisodormedasilva.domain.FinancialDirection
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    state: CalendarState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = Locale.getDefault()
    val monthLabel = state.month.format(DateTimeFormatter.ofPattern("MMMM uuuu", locale))
    val dateLabel = state.selectedDate.format(DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.LONG))
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 12.dp).testTag("calendar"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text(stringResource(R.string.calendar_title), style = MaterialTheme.typography.headlineSmall) }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onPreviousMonth, modifier = Modifier.testTag("previous-month")) {
                    Text(stringResource(R.string.previous_month))
                }
                Text(monthLabel, Modifier.weight(1f).testTag("month-title"), style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onNextMonth, modifier = Modifier.testTag("next-month")) {
                    Text(stringResource(R.string.next_month))
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth()) {
                DayOfWeek.entries.forEach { day ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(day.getDisplayName(TextStyle.SHORT, locale))
                    }
                }
            }
        }
        items(monthSlots(state.month).chunked(7)) { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (date != null) {
                            val items = state.itemsByDate[date].orEmpty()
                            val inflows = items.count { it.direction == FinancialDirection.INFLOW }
                            val outflows = items.size - inflows
                            val description = stringResource(R.string.date_summary, date.toString(), inflows, outflows)
                            Surface(
                                onClick = { onSelectDate(date) },
                                color = if (date == state.selectedDate) MaterialTheme.colorScheme.secondaryContainer
                                    else MaterialTheme.colorScheme.surface,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)
                                    .testTag("day-$date")
                                    .semantics {
                                        contentDescription = description
                                        selected = date == state.selectedDate
                                    },
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 6.dp)) {
                                    Text(date.dayOfMonth.toString())
                                    if (inflows > 0) Text(stringResource(R.string.inflow_count, inflows), style = MaterialTheme.typography.labelSmall)
                                    if (outflows > 0) Text(stringResource(R.string.outflow_count, outflows), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Text(dateLabel, style = MaterialTheme.typography.titleMedium) }
        when {
            state.isLoading -> item { Text(stringResource(R.string.loading)) }
            state.hasLoadError -> item { Text(stringResource(R.string.load_error)) }
            state.selectedItems.isEmpty() -> item { Text(stringResource(R.string.empty_day)) }
            else -> items(state.selectedItems, key = { it.id }) { item ->
                Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(item.label, style = MaterialTheme.typography.bodyLarge)
                    Text(stringResource(if (item.direction == FinancialDirection.INFLOW) R.string.expected_inflow else R.string.expected_outflow))
                }
            }
        }
    }
}
