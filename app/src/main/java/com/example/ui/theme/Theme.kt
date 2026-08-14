package com.example.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

@Composable
fun ExpressiveTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    seedColor: MonetSeedColor = MonetSeedColor.PURPLE,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val rawColorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getExpressiveColorScheme(seedColor, isDark)
    }

    // Smooth transition between themes
    val animatedColorScheme = animateColorScheme(rawColorScheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
private fun animateColorScheme(target: ColorScheme): ColorScheme {
    val animSpec = spring<androidx.compose.ui.graphics.Color>(stiffness = 300f)

    val primary = animateColorAsState(target.primary, animSpec, label = "primary").value
    val onPrimary = animateColorAsState(target.onPrimary, animSpec, label = "onPrimary").value
    val primaryContainer = animateColorAsState(target.primaryContainer, animSpec, label = "primaryContainer").value
    val onPrimaryContainer = animateColorAsState(target.onPrimaryContainer, animSpec, label = "onPrimaryContainer").value
    val secondary = animateColorAsState(target.secondary, animSpec, label = "secondary").value
    val onSecondary = animateColorAsState(target.onSecondary, animSpec, label = "onSecondary").value
    val secondaryContainer = animateColorAsState(target.secondaryContainer, animSpec, label = "secondaryContainer").value
    val onSecondaryContainer = animateColorAsState(target.onSecondaryContainer, animSpec, label = "onSecondaryContainer").value
    val tertiary = animateColorAsState(target.tertiary, animSpec, label = "tertiary").value
    val onTertiary = animateColorAsState(target.onTertiary, animSpec, label = "onTertiary").value
    val tertiaryContainer = animateColorAsState(target.tertiaryContainer, animSpec, label = "tertiaryContainer").value
    val onTertiaryContainer = animateColorAsState(target.onTertiaryContainer, animSpec, label = "onTertiaryContainer").value
    val background = animateColorAsState(target.background, animSpec, label = "background").value
    val onBackground = animateColorAsState(target.onBackground, animSpec, label = "onBackground").value
    val surface = animateColorAsState(target.surface, animSpec, label = "surface").value
    val onSurface = animateColorAsState(target.onSurface, animSpec, label = "onSurface").value
    val surfaceVariant = animateColorAsState(target.surfaceVariant, animSpec, label = "surfaceVariant").value
    val onSurfaceVariant = animateColorAsState(target.onSurfaceVariant, animSpec, label = "onSurfaceVariant").value
    val outline = animateColorAsState(target.outline, animSpec, label = "outline").value
    val outlineVariant = animateColorAsState(target.outlineVariant, animSpec, label = "outlineVariant").value

    return target.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant
    )
}

