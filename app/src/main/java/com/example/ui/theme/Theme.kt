package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ProDarkPrimary,
    onPrimary = ProDarkOnPrimaryContainer,
    secondary = ProDarkSecondary,
    onSecondary = ProDarkBackground,
    background = ProDarkBackground,
    surface = ProDarkSurface,
    onBackground = ProDarkOnBackground,
    onSurface = ProDarkOnSurface,
    primaryContainer = ProDarkPrimaryContainer,
    onPrimaryContainer = ProDarkOnPrimaryContainer,
    outline = ProDarkOutline,
    secondaryContainer = ProDarkSecondaryContainer,
    onSecondaryContainer = ProDarkOnSecondaryContainer
)

private val LightColorScheme = lightColorScheme(
    primary = ProPrimary,
    onPrimary = Color.White,
    secondary = ProSecondary,
    onSecondary = Color.White,
    background = ProBackground,
    surface = ProSurface,
    onBackground = ProOnBackground,
    onSurface = ProOnSurface,
    primaryContainer = ProPrimaryContainer,
    onPrimaryContainer = ProOnPrimaryContainer,
    outline = ProOutline,
    secondaryContainer = ProSecondaryContainer,
    onSecondaryContainer = ProOnSecondaryContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
