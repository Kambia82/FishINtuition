package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NcLime,
    onPrimary = NcBg,
    primaryContainer = NcLimeDim,
    onPrimaryContainer = Color.White,
    secondary = HydroCyan,
    onSecondary = NcBg,
    secondaryContainer = Color(0xFF004D40),
    onSecondaryContainer = Color.White,
    tertiary = StrikeAmber,
    onTertiary = NcBg,
    background = NcBg,
    onBackground = TextPrimary,
    surface = NcCard,
    onSurface = TextPrimary,
    surfaceVariant = NcCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = NcBorder,
    outlineVariant = NcBorderSubtle,
    error = WarningRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
