package com.talisodormedasilva.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talisodormedasilva.R
import com.talisodormedasilva.domain.FinancialDirection
import com.talisodormedasilva.domain.FinancialItem
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val CalendarLocale: Locale = Locale.forLanguageTag("pt-BR")
internal fun money(cents: Long): String = NumberFormat.getCurrencyInstance(CalendarLocale)
    .format(BigDecimal.valueOf(cents, 2))
private fun String.capitalized() = replaceFirstChar { it.titlecase(CalendarLocale) }

@Composable
fun CalendarScreen(
    state: CalendarState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    onOpenAddItem: () -> Unit = {},
    onCloseAddItem: () -> Unit = {},
    onSaveItem: (String, FinancialDirection, Long, LocalDate) -> Unit = { _, _, _, _ -> },
) {
    val month = state.month.format(DateTimeFormatter.ofPattern("MMMM", CalendarLocale)).capitalized()
    Box(modifier.fillMaxSize().background(CalendarBackground)) {
        Box(Modifier.offset(x = (-24).dp).size(92.dp).background(
            Brush.radialGradient(listOf(Color(0xFFeed6f2), Color(0xFFcba7dc).copy(alpha = .6f))), CircleShape,
        ))
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("calendar"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(month, fontSize = 34.sp, lineHeight = 37.sp, letterSpacing = (-1.2).sp,
                        fontWeight = FontWeight.Bold, color = Color(0xFF2A1D2E))
                    Text(stringResource(R.string.month_subtitle, state.month.year), fontSize = 14.sp, color = CalendarMuted)
                }
            }
            item {
                val monthlyItems = state.itemsByDate.filterKeys { java.time.YearMonth.from(it) == state.month }.values.flatten()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCard(monthlyItems, FinancialDirection.OUTFLOW, state, Modifier.weight(1f))
                    SummaryCard(monthlyItems, FinancialDirection.INFLOW, state, Modifier.weight(1f))
                }
            }
            item {
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 3.dp) {
                    Row(Modifier.fillMaxWidth().padding(4.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        MonthButton("‹", stringResource(R.string.previous_month), "previous-month", onPreviousMonth)
                        Text("$month ${state.month.year}", Modifier.testTag("month-title"),
                            fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk)
                        MonthButton("›", stringResource(R.string.next_month), "next-month", onNextMonth)
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().background(Color(0xFFF3E6F5), RoundedCornerShape(30.dp)).padding(10.dp, 18.dp)) {
                    Row(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        listOf("S", "T", "Q", "Q", "S", "S", "D").forEach {
                            Box(Modifier.weight(1f).height(24.dp), contentAlignment = Alignment.Center) {
                                Text(it, color = Color(0xFF8A788E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    monthSlots(state.month).chunked(7).forEach { week ->
                        Row(Modifier.fillMaxWidth()) {
                            week.forEach { date ->
                                Box(Modifier.weight(1f).height(48.dp), contentAlignment = Alignment.Center) {
                                    if (date != null) {
                                        val entries = state.itemsByDate[date].orEmpty()
                                        val inflows = entries.count { it.direction == FinancialDirection.INFLOW }
                                        val description = stringResource(R.string.date_summary,
                                            date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' uuuu", CalendarLocale)),
                                            inflows, entries.size - inflows)
                                        val chosen = date == state.selectedDate
                                        Surface(onClick = { onSelectDate(date) },
                                            modifier = Modifier.fillMaxSize().testTag("day-$date").semantics {
                                                contentDescription = description
                                                selected = chosen
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color.Transparent,
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Box(Modifier.size(36.dp).background(
                                                    if (chosen) CalendarPurple else Color.Transparent, RoundedCornerShape(12.dp)),
                                                    contentAlignment = Alignment.Center) {
                                                    Text(date.dayOfMonth.toString(), fontSize = 14.sp,
                                                        fontWeight = if (chosen) FontWeight.SemiBold else FontWeight.Medium,
                                                        color = if (chosen) Color.White else CalendarInk)
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
                // FlowRow keeps the weekday readable with large fonts and narrow screens.
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(state.selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' uuuu", CalendarLocale)),
                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk)
                    Text(state.selectedDate.format(DateTimeFormatter.ofPattern("EEEE", CalendarLocale)).capitalized(),
                        Modifier.background(Color(0xFFF1EBF8), CircleShape).padding(horizontal = 10.dp, vertical = 5.dp),
                        color = CalendarPurple, fontSize = 11.sp)
                }
            }
            when {
                state.isLoading -> item { MessageCard(stringResource(R.string.loading)) }
                state.hasLoadError -> item { MessageCard(stringResource(R.string.load_error)) }
                state.selectedItems.isEmpty() -> item {
                    MessageCard(stringResource(R.string.empty_day), stringResource(R.string.empty_day_description))
                }
                else -> items(state.selectedItems, key = { it.id }) { item -> FinancialRow(item) }
            }
            item {
                Button(onClick = onOpenAddItem, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).testTag("add-item"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF17141B), contentColor = Color.White)) {
                    Text("+", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.add_item), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
    if (state.isAdding) {
        AddItemSheet(state.selectedDate, state.isSaving, state.saveError, onCloseAddItem, onSaveItem)
    }
}

@Composable
private fun MonthButton(symbol: String, description: String, tag: String, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(12.dp), color = Color(0xFFF5F0FB),
        modifier = Modifier.size(48.dp).testTag(tag).semantics { contentDescription = description }) {
        Box(contentAlignment = Alignment.Center) { Text(symbol, fontSize = 26.sp, color = CalendarPurple) }
    }
}

@Composable
private fun SummaryCard(items: List<FinancialItem>, direction: FinancialDirection, state: CalendarState, modifier: Modifier) {
    val entries = items.filter { it.direction == direction }
    val outflow = direction == FinancialDirection.OUTFLOW
    Column(modifier.background(if (outflow) Color(0xFFF2D9E7) else Color(0xFFDDEAD7), RoundedCornerShape(24.dp))
        .heightIn(min = 88.dp).padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(stringResource(if (outflow) R.string.expected_outflow else R.string.expected_inflow), fontSize = 11.sp, color = CalendarMuted)
        val amount = when {
            state.isLoading || state.hasLoadError -> "—"
            entries.any { it.amountCents == null } -> stringResource(R.string.amount_unavailable)
            else -> NumberFormat.getCurrencyInstance(CalendarLocale).format(
                entries.fold(BigDecimal.ZERO) { sum, item -> sum + BigDecimal.valueOf(item.amountCents!!, 2) })
        }
        Text(amount, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk,
            modifier = Modifier.testTag(if (outflow) "total-due" else "total-incoming"))
        if (!state.isLoading && !state.hasLoadError) {
            Text(pluralStringResource(if (outflow) R.plurals.commitments else R.plurals.incomes, entries.size, entries.size),
                fontSize = 10.sp, color = CalendarMuted)
        }
    }
}

@Composable
private fun MessageCard(title: String, description: String? = null) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFF7F4FA), RoundedCornerShape(16.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk)
        if (description != null) Text(description, fontSize = 13.sp, lineHeight = 19.sp, color = CalendarMuted)
    }
}

@Composable
private fun FinancialRow(item: FinancialItem) {
    Row(Modifier.fillMaxWidth().background(Color(0xFFF7F4FA), RoundedCornerShape(16.dp)).padding(16.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(item.label, fontWeight = FontWeight.SemiBold, color = CalendarInk)
            Text(stringResource(if (item.direction == FinancialDirection.INFLOW) R.string.expected_inflow else R.string.expected_outflow),
                fontSize = 12.sp, color = CalendarMuted)
        }
        Text(item.amountCents?.let(::money) ?: stringResource(R.string.amount_unavailable),
            fontWeight = FontWeight.SemiBold, color = CalendarInk)
    }
}
