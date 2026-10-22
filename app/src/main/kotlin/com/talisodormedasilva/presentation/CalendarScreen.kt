package com.talisodormedasilva.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
    Box(modifier.fillMaxSize().background(CalendarBackground)) {
        Box(Modifier.offset(x = (-24).dp).size(92.dp).background(
            Brush.radialGradient(listOf(Color(0xFFeed6f2), Color(0xFFcba7dc).copy(alpha = .6f))), CircleShape,
        ))
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("calendar"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(
                    Modifier.fillMaxWidth()
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
                        .background(Color(0xFFF3E6F5), RoundedCornerShape(30.dp))
                        .testTag("month-grid")
                        .semantics {
                            contentDescription = state.month.format(
                                DateTimeFormatter.ofPattern("MMMM uuuu", CalendarLocale),
                            )
                        }
                        .padding(10.dp, 18.dp),
                ) {
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
            if (state.hasLoadError) {
                item { MessageCard(stringResource(R.string.load_error)) }
            }
        }
    }
    if (state.isAdding) {
        AddItemSheet(state.selectedDate, state.isSaving, state.saveError, onCloseAddItem, onSaveItem)
    }
}

@Composable
private fun MessageCard(title: String) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFF7F4FA), RoundedCornerShape(16.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = CalendarInk)
    }
}

