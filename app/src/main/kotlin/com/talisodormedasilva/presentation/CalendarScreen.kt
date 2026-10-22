package com.talisodormedasilva.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talisodormedasilva.R
import com.talisodormedasilva.domain.FinancialDirection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val CalendarLocale: Locale = Locale.forLanguageTag("pt-BR")
private fun String.capitalized() = replaceFirstChar { it.titlecase(CalendarLocale) }

@Composable
fun CalendarScreen(
    state: CalendarState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    onCloseAddItem: () -> Unit = {},
    onSaveItem: (String, FinancialDirection, Long, LocalDate) -> Unit = { _, _, _, _ -> },
) {
    val monthTitle = state.month.format(DateTimeFormatter.ofPattern("MMMM", CalendarLocale)).capitalized()
    Box(modifier.fillMaxSize().background(CalendarBackground)) {
        Box(Modifier.offset(x = (-24).dp).size(92.dp).background(
            Brush.radialGradient(listOf(Color(0xFFeed6f2), Color(0xFFcba7dc).copy(alpha = .6f))), CircleShape,
        ))
        Column(
            modifier = Modifier.fillMaxSize().testTag("calendar")
                .padding(start = 16.dp, end = 16.dp, top = 72.dp, bottom = 22.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    monthTitle,
                    modifier = Modifier.testTag("month-heading"),
                    fontSize = 34.sp, lineHeight = 37.sp, letterSpacing = (-1.2).sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF2A1D2E),
                )
                Text(
                    stringResource(R.string.month_subtitle, state.month.year),
                    modifier = Modifier.testTag("month-subtitle"),
                    fontSize = 14.sp, color = CalendarMuted,
                )
            }
            Spacer(Modifier.height(20.dp))
            val weeks = monthSlots(state.month).chunked(7)
            BoxWithConstraints(Modifier.fillMaxWidth().weight(1f)) {
                // Compact cards accommodate months spanning six calendar rows.
                val availablePerWeek = (maxHeight - 8.dp * (weeks.size - 1)) / weeks.size
                val compact = availablePerWeek < 88.dp
                Column(
                    modifier = Modifier.fillMaxSize()
                        .pointerInput(state.month, onPreviousMonth, onNextMonth) {
                            val threshold = 64.dp.toPx()
                            var totalDrag = 0f
                            detectHorizontalDragGestures(
                                onDragStart = { totalDrag = 0f },
                                onDragEnd = {
                                    when {
                                        totalDrag < -threshold -> onNextMonth()
                                        totalDrag > threshold -> onPreviousMonth()
                                    }
                                },
                                onDragCancel = { totalDrag = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    totalDrag += dragAmount
                                },
                            )
                        }
                        .testTag("month-grid")
                        .semantics {
                            contentDescription = state.month.format(
                                DateTimeFormatter.ofPattern("MMMM uuuu", CalendarLocale),
                            )
                        },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    weeks.forEachIndexed { index, week ->
                        WeekCard(
                            weekNumber = index + 1,
                            dates = week,
                            state = state,
                            onSelectDate = onSelectDate,
                            compact = compact,
                            modifier = Modifier.fillMaxWidth().weight(1f),
                        )
                    }
                }
            }
            if (state.hasLoadError) {
                Spacer(Modifier.height(8.dp))
                MessageCard(stringResource(R.string.load_error))
            }
        }
    }
    if (state.isAdding) {
        AddItemSheet(state.selectedDate, state.isSaving, state.saveError, onCloseAddItem, onSaveItem)
    }
}


@Composable
private fun WeekCard(
    weekNumber: Int,
    dates: List<LocalDate?>,
    state: CalendarState,
    onSelectDate: (LocalDate) -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.testTag("week-$weekNumber"),
        color = Color(0xFFF9F2FA),
        shape = RoundedCornerShape(23.dp),
        border = BorderStroke(1.dp, Color(0xFFD9C9E1)),
    ) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = if (compact) 4.dp else 8.dp),
        ) {
            Surface(
                color = Color(0xFFEADCF0),
                shape = RoundedCornerShape(18.dp),
            ) {
                Text(
                    text = "Semana $weekNumber",
                    modifier = Modifier.testTag("week-label-$weekNumber")
                        .padding(horizontal = 11.dp, vertical = if (compact) 2.dp else 4.dp),
                    fontSize = if (compact) 12.sp else 13.sp,
                    lineHeight = if (compact) 14.sp else 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF766282),
                )
            }
            Row(Modifier.fillMaxWidth().height(if (compact) 15.dp else 18.dp)) {
                listOf("S", "T", "Q", "Q", "S", "S", "D").forEach { label ->
                    Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(label, color = Color(0xFF8A788E), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().weight(1f)) {
                dates.forEach { date ->
                    Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        if (date != null) {
                            val entries = state.itemsByDate[date].orEmpty()
                            val inflows = entries.count { it.direction == FinancialDirection.INFLOW }
                            val description = stringResource(
                                R.string.date_summary,
                                date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' uuuu", CalendarLocale)),
                                inflows,
                                entries.size - inflows,
                            )
                            val chosen = date == state.selectedDate
                            Surface(
                                onClick = { onSelectDate(date) },
                                modifier = Modifier.fillMaxSize().testTag("day-$date").semantics {
                                    contentDescription = description
                                    selected = chosen
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Box(
                                        modifier = Modifier
                                            .size(if (compact) 30.dp else 36.dp)
                                            .background(
                                                if (chosen) CalendarPurple else Color.Transparent,
                                                RoundedCornerShape(12.dp),
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = date.dayOfMonth.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = if (chosen) FontWeight.SemiBold else FontWeight.Medium,
                                            color = if (chosen) Color.White else CalendarInk,
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

@Composable
private fun MessageCard(title: String) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFF7F4FA), RoundedCornerShape(16.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk)
    }
}

