package com.talisodormedasilva.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val CalendarBackground = Color(0xFFFFF8FF)
internal val CalendarPurple = Color(0xFF6E4AA1)
internal val CalendarInk = Color(0xFF211D25)
internal val CalendarMuted = Color(0xFF7B6C80)

@Composable
fun CalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = CalendarPurple,
            onPrimary = Color.White,
            background = CalendarBackground,
            onBackground = CalendarInk,
            surface = CalendarBackground,
            onSurface = CalendarInk,
            surfaceVariant = Color(0xFFF3E6F5),
            onSurfaceVariant = CalendarMuted,
            secondaryContainer = Color(0xFFF1EBF8),
            onSecondaryContainer = CalendarPurple,
        ),
        content = content,
    )
}
