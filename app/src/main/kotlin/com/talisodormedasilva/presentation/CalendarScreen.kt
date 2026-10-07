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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
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
    val monthName = state.month.format(DateTimeFormatter.ofPattern("MMMM", locale))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    val monthYear = state.month.format(DateTimeFormatter.ofPattern("MMMM uuuu", locale))
    val dateLabel = state.selectedDate.format(
        DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.LONG),
    )
    val monthItems = state.itemsByDate.values.flatten()
    val inflowCount = monthItems.count { it.direction == FinancialDirection.INFLOW }
    val outflowCount = monthItems.size - inflowCount

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 20.dp,
            bottom = 28.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.month_financial_summary, state.month.year),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("month-summary"),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SummaryCard(
                    label = stringResource(R.string.incoming),
                    value = stringResource(R.string.item_count, inflowCount),
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                SummaryCard(
                    label = stringResource(R.string.outgoing),
                    value = stringResource(R.string.item_count, outflowCount),
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("month-navigation"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    TextButton(
                        onClick = onPreviousMonth,
                        modifier = Modifier.testTag("previous-month"),
                    ) {
                        Text("‹", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                Text(
                    text = monthYear,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("month-title"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    TextButton(
                        onClick = onNextMonth,
                        modifier = Modifier.testTag("next-month"),
                    ) {
                        Text("›", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calendar-card"),
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.58f),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        DayOfWeek.entries.forEach { day ->
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    text = day.getDisplayName(TextStyle.NARROW, locale),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    monthSlots(state.month).chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            week.forEach { date ->
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (date != null) {
                                        val dateItems = state.itemsByDate[date].orEmpty()
                                        val dayInflows = dateItems.count {
                                            it.direction == FinancialDirection.INFLOW
                                        }
                                        val dayOutflows = dateItems.size - dayInflows
                                        val description = stringResource(
                                            R.string.date_summary,
                                            date.toString(),
                                            dayInflows,
                                            dayOutflows,
                                        )
                                        val isSelected = date == state.selectedDate

                                        Surface(
                                            onClick = { onSelectDate(date) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(min = 46.dp)
                                                .clip(CircleShape)
                                                .testTag("day-$date")
                                                .semantics {
                                                    contentDescription = description
                                                    selected = isSelected
                                                },
                                            shape = CircleShape,
                                            color = if (isSelected) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                androidx.compose.ui.graphics.Color.Transparent
                                            },
                                            contentColor = if (isSelected) {
                                                MaterialTheme.colorScheme.onPrimary
                                            } else {
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                            },
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(vertical = 6.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                            ) {
                                                Text(
                                                    text = date.dayOfMonth.toString(),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (isSelected) {
                                                        FontWeight.Bold
                                                    } else {
                                                        FontWeight.Medium
                                                    },
                                                )
                                                if (dateItems.isNotEmpty()) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                                                    ) {
                                                        if (dayInflows > 0) {
                                                            EventDot(
                                                                color = if (isSelected) {
                                                                    MaterialTheme.colorScheme.onPrimary
                                                                } else {
                                                                    MaterialTheme.colorScheme.tertiary
                                                                },
                                                            )
                                                        }
                                                        if (dayOutflows > 0) {
                                                            EventDot(
                                                                color = if (isSelected) {
                                                                    MaterialTheme.colorScheme.onPrimary
                                                                } else {
                                                                    MaterialTheme.colorScheme.primary
                                                                },
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("day-sheet"),
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )

                    when {
                        state.isLoading -> Text(
                            stringResource(R.string.loading),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        state.hasLoadError -> Text(
                            stringResource(R.string.load_error),
                            color = MaterialTheme.colorScheme.error,
                        )

                        state.selectedItems.isEmpty() -> Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.empty_day_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = stringResource(R.string.empty_day),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        else -> state.selectedItems.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(3.dp),
                                ) {
                                    Text(
                                        item.label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        stringResource(
                                            if (item.direction == FinancialDirection.INFLOW) {
                                                R.string.expected_inflow
                                            } else {
                                                R.string.expected_outflow
                                            },
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    modifier: Modifier,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
) {
    Surface(
        modifier = modifier.heightIn(min = 88.dp),
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun EventDot(color: androidx.compose.ui.graphics.Color) {
    Surface(
        modifier = Modifier.padding(top = 1.dp),
        shape = CircleShape,
        color = color,
    ) {
        Box(Modifier.padding(2.dp))
    }
}
