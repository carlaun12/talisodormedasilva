package com.talisodormedasilva.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ExpressiveLightColors = lightColorScheme(
    primary = Color(0xFF74427D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF0D9F4),
    onPrimaryContainer = Color(0xFF2D1533),
    secondary = Color(0xFF765879),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0E1F2),
    onSecondaryContainer = Color(0xFF2E2132),
    tertiary = Color(0xFF56704D),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDDEAD7),
    onTertiaryContainer = Color(0xFF1E3519),
    background = Color(0xFFFFF8FF),
    onBackground = Color(0xFF241A26),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF241A26),
    surfaceVariant = Color(0xFFF6EFF7),
    onSurfaceVariant = Color(0xFF6D626F),
)

private val ExpressiveShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(30.dp),
)

@Composable
fun CalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ExpressiveLightColors,
        shapes = ExpressiveShapes,
        content = content,
    )
}
