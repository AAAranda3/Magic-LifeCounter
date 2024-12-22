package com.example.lifecounting.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Dark Mode Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE2DFDF),      // Colorless (Neutral Silver)
    secondary = Color(0xFF1C1C1C),    // Black (Dark Swamp)
    tertiary = Color(0xFF4D88B2),     // Blue (Deep Ocean)
    background = Color(0xFF121212),   // Dark Base
    surface = Color(0xFF2B2B2B),      // Dark Surface
    onPrimary = Color.Black,          // Text on Primary
    onSecondary = Color.White,        // Text on Secondary
    onTertiary = Color.White,         // Text on Tertiary
    onBackground = Color(0xFFDADADA), // Text on Background
    onSurface = Color(0xFFDADADA)     // Text on Surface
)

// Light Mode Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4B8246),      // Green (Forest)
    secondary = Color(0xFFCC4235),    // Red (Mountain)
    tertiary = Color(0xFFF7F3E8),     // White (Plains)
    background = Color(0xFFFFFBFE),   // Light Base
    surface = Color(0xFFFFFBFE),      // Light Surface
    onPrimary = Color.White,          // Text on Primary
    onSecondary = Color.Black,        // Text on Secondary
    onTertiary = Color.Black,         // Text on Tertiary
    onBackground = Color.Black,       // Text on Background
    onSurface = Color.Black           // Text on Surface
)

@Composable
fun LifeCountingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}