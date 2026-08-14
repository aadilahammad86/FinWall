package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Default Purple Scheme
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

enum class MonetSeedColor(val displayName: String, val primaryColor: Color) {
    PURPLE("Monet Purple", Color(0xFF6750A4)),
    CORAL("Expressive Coral", Color(0xFFE0533C)),
    MINT("Emerald Mint", Color(0xFF00897B)),
    CYAN("Cosmic Cyan", Color(0xFF00838F)),
    AMBER("Sunset Amber", Color(0xFFD84315)),
    ROSE("Vibrant Rose", Color(0xFFD81B60)),
    INDIGO("Royal Indigo", Color(0xFF3F51B5)),
    TEAL("Pacific Teal", Color(0xFF009688))
}

fun getExpressiveColorScheme(seed: MonetSeedColor, isDark: Boolean) = when (seed) {
    MonetSeedColor.PURPLE -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFFD0BCFF),
            onPrimary = Color(0xFF381E72),
            primaryContainer = Color(0xFF4F378B),
            onPrimaryContainer = Color(0xFFEADDFF),
            secondary = Color(0xFFCCC2DC),
            onSecondary = Color(0xFF332D41),
            secondaryContainer = Color(0xFF4A4458),
            onSecondaryContainer = Color(0xFFE8DEF8),
            tertiary = Color(0xFFEFB8C8),
            onTertiary = Color(0xFF492532),
            tertiaryContainer = Color(0xFF633B48),
            onTertiaryContainer = Color(0xFFFFD8E4),
            background = Color(0xFF141218),
            onBackground = Color(0xFFE6E0E9),
            surface = Color(0xFF141218),
            onSurface = Color(0xFFE6E0E9),
            surfaceVariant = Color(0xFF49454F),
            onSurfaceVariant = Color(0xFFCAC4D0)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6750A4),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFEADDFF),
            onPrimaryContainer = Color(0xFF21005D),
            secondary = Color(0xFF625B71),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFE8DEF8),
            onSecondaryContainer = Color(0xFF1D192B),
            tertiary = Color(0xFF7D5260),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFD8E4),
            onTertiaryContainer = Color(0xFF31111D),
            background = Color(0xFFFEF7FF),
            onBackground = Color(0xFF1D1B20),
            surface = Color(0xFFFEF7FF),
            onSurface = Color(0xFF1D1B20),
            surfaceVariant = Color(0xFFE7E0EC),
            onSurfaceVariant = Color(0xFF49454F)
        )
    }

    MonetSeedColor.CORAL -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFFFFB4A8),
            onPrimary = Color(0xFF561E16),
            primaryContainer = Color(0xFF73342A),
            onPrimaryContainer = Color(0xFFFFDAD4),
            secondary = Color(0xFFE7BDB6),
            onSecondary = Color(0xFF442925),
            secondaryContainer = Color(0xFF5D3F3B),
            onSecondaryContainer = Color(0xFFFFDAD4),
            tertiary = Color(0xFFDEC48C),
            onTertiary = Color(0xFF3E2E04),
            tertiaryContainer = Color(0xFF564419),
            onTertiaryContainer = Color(0xFFFCE0A6),
            background = Color(0xFF201A19),
            onBackground = Color(0xFFEDECEB),
            surface = Color(0xFF201A19),
            onSurface = Color(0xFFEDECEB),
            surfaceVariant = Color(0xFF534341),
            onSurfaceVariant = Color(0xFFD8C2BE)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF904A41),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDAD4),
            onPrimaryContainer = Color(0xFF3B0906),
            secondary = Color(0xFF775651),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDAD4),
            onSecondaryContainer = Color(0xFF2C1512),
            tertiary = Color(0xFF705C2E),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFCE0A6),
            onTertiaryContainer = Color(0xFF251A00),
            background = Color(0xFFFFF8F6),
            onBackground = Color(0xFF231A18),
            surface = Color(0xFFFFF8F6),
            onSurface = Color(0xFF231A18),
            surfaceVariant = Color(0xFFF5DDDA),
            onSurfaceVariant = Color(0xFF534341)
        )
    }

    MonetSeedColor.MINT -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFF80D5C4),
            onPrimary = Color(0xFF00372E),
            primaryContainer = Color(0xFF005044),
            onPrimaryContainer = Color(0xFF9CF1DF),
            secondary = Color(0xFFB1CCC6),
            onSecondary = Color(0xFF1C3530),
            secondaryContainer = Color(0xFF334B46),
            onSecondaryContainer = Color(0xFFCDE8E2),
            tertiary = Color(0xFFB4C8E8),
            onTertiary = Color(0xFF1E314B),
            tertiaryContainer = Color(0xFF354863),
            onTertiaryContainer = Color(0xFFD4E3FF),
            background = Color(0xFF0F1513),
            onBackground = Color(0xFFDEE4E1),
            surface = Color(0xFF0F1513),
            onSurface = Color(0xFFDEE4E1),
            surfaceVariant = Color(0xFF3F4946),
            onSurfaceVariant = Color(0xFFBEC9C5)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF006A5B),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFF9CF1DF),
            onPrimaryContainer = Color(0xFF00201A),
            secondary = Color(0xFF4A635D),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFCDE8E2),
            onSecondaryContainer = Color(0xFF06201B),
            tertiary = Color(0xFF4D607C),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFD4E3FF),
            onTertiaryContainer = Color(0xFF061C35),
            background = Color(0xFFF4FAF7),
            onBackground = Color(0xFF171D1B),
            surface = Color(0xFFF4FAF7),
            onSurface = Color(0xFF171D1B),
            surfaceVariant = Color(0xFFDAE5E1),
            onSurfaceVariant = Color(0xFF3F4946)
        )
    }

    MonetSeedColor.CYAN -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFF80D4F6),
            onPrimary = Color(0xFF003548),
            primaryContainer = Color(0xFF004D67),
            onPrimaryContainer = Color(0xFFBBE9FF),
            secondary = Color(0xFFB3CAD5),
            onSecondary = Color(0xFF1E333C),
            secondaryContainer = Color(0xFF354A53),
            onSecondaryContainer = Color(0xFFCEE6F1),
            tertiary = Color(0xFFC3C3EB),
            onTertiary = Color(0xFF2C2D4D),
            tertiaryContainer = Color(0xFF434365),
            onTertiaryContainer = Color(0xFFE1E0FF),
            background = Color(0xFF0E1417),
            onBackground = Color(0xFFDEE3E6),
            surface = Color(0xFF0E1417),
            onSurface = Color(0xFFDEE3E6),
            surfaceVariant = Color(0xFF40484C),
            onSurfaceVariant = Color(0xFFC0C8CC)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF006688),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFBBE9FF),
            onPrimaryContainer = Color(0xFF001F2B),
            secondary = Color(0xFF4C616B),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFCEE6F1),
            onSecondaryContainer = Color(0xFF081E26),
            tertiary = Color(0xFF5B5B7E),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFE1E0FF),
            onTertiaryContainer = Color(0xFF181837),
            background = Color(0xFFF5FAFD),
            onBackground = Color(0xFF171C1F),
            surface = Color(0xFFF5FAFD),
            onSurface = Color(0xFF171C1F),
            surfaceVariant = Color(0xFFDCE4E8),
            onSurfaceVariant = Color(0xFF40484C)
        )
    }

    MonetSeedColor.AMBER -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFFFFB59D),
            onPrimary = Color(0xFF591D08),
            primaryContainer = Color(0xFF77331C),
            onPrimaryContainer = Color(0xFFFFDBCF),
            secondary = Color(0xFFE7BEB2),
            onSecondary = Color(0xFF442A22),
            secondaryContainer = Color(0xFF5D4037),
            onSecondaryContainer = Color(0xFFFFDBCF),
            tertiary = Color(0xFFD5C68E),
            onTertiary = Color(0xFF393006),
            tertiaryContainer = Color(0xFF50461B),
            onTertiaryContainer = Color(0xFFF2E2A8),
            background = Color(0xFF1A110E),
            onBackground = Color(0xFFF0DFDA),
            surface = Color(0xFF1A110E),
            onSurface = Color(0xFFF0DFDA),
            surfaceVariant = Color(0xFF53433F),
            onSurfaceVariant = Color(0xFFD8C2BC)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFA04222),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDBCF),
            onPrimaryContainer = Color(0xFF3B0900),
            secondary = Color(0xFF77574E),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDBCF),
            onSecondaryContainer = Color(0xFF2C160F),
            tertiary = Color(0xFF695E2F),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFF2E2A8),
            onTertiaryContainer = Color(0xFF211B00),
            background = Color(0xFFFFF8F6),
            onBackground = Color(0xFF231A17),
            surface = Color(0xFFFFF8F6),
            onSurface = Color(0xFF231A17),
            surfaceVariant = Color(0xFFF5DDD7),
            onSurfaceVariant = Color(0xFF53433F)
        )
    }

    MonetSeedColor.ROSE -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFFFFB0C8),
            onPrimary = Color(0xFF5E1133),
            primaryContainer = Color(0xFF7B2949),
            onPrimaryContainer = Color(0xFFFFD9E2),
            secondary = Color(0xFFE5BDC5),
            onSecondary = Color(0xFF432930),
            secondaryContainer = Color(0xFF5C3F46),
            onSecondaryContainer = Color(0xFFFFD9E2),
            tertiary = Color(0xFFE2C28C),
            onTertiary = Color(0xFF402D04),
            tertiaryContainer = Color(0xFF594318),
            onTertiaryContainer = Color(0xFFFFDEA7),
            background = Color(0xFF1F1014),
            onBackground = Color(0xFFF0DEE1),
            surface = Color(0xFF1F1014),
            onSurface = Color(0xFFF0DEE1),
            surfaceVariant = Color(0xFF524346),
            onSurfaceVariant = Color(0xFFD6C2C5)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF982560),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFD9E2),
            onPrimaryContainer = Color(0xFF3E001D),
            secondary = Color(0xFF75565E),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFD9E2),
            onSecondaryContainer = Color(0xFF2B151C),
            tertiary = Color(0xFF735A2C),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFDEA7),
            onTertiaryContainer = Color(0xFF261900),
            background = Color(0xFFFFF8F8),
            onBackground = Color(0xFF22191C),
            surface = Color(0xFFFFF8F8),
            onSurface = Color(0xFF22191C),
            surfaceVariant = Color(0xFFF2DDE1),
            onSurfaceVariant = Color(0xFF524346)
        )
    }

    MonetSeedColor.INDIGO -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFFBAC3FF),
            onPrimary = Color(0xFF212C6E),
            primaryContainer = Color(0xFF394386),
            onPrimaryContainer = Color(0xFFDEE0FF),
            secondary = Color(0xFFC3C5DD),
            onSecondary = Color(0xFF2D2F42),
            secondaryContainer = Color(0xFF434559),
            onSecondaryContainer = Color(0xFFE0E1F9),
            tertiary = Color(0xFFE4BAD7),
            onTertiary = Color(0xFF44263D),
            tertiaryContainer = Color(0xFF5C3C54),
            onTertiaryContainer = Color(0xFFFFD7F3),
            background = Color(0xFF12131C),
            onBackground = Color(0xFFE3E1EC),
            surface = Color(0xFF12131C),
            onSurface = Color(0xFFE3E1EC),
            surfaceVariant = Color(0xFF46464F),
            onSurfaceVariant = Color(0xFFC6C5D0)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF515B9F),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFDEE0FF),
            onPrimaryContainer = Color(0xFF0B1457),
            secondary = Color(0xFF5B5D72),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFE0E1F9),
            onSecondaryContainer = Color(0xFF181A2C),
            tertiary = Color(0xFF76536C),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFD7F3),
            onTertiaryContainer = Color(0xFF2C1127),
            background = Color(0xFFFEF7FF),
            onBackground = Color(0xFF1B1B21),
            surface = Color(0xFFFEF7FF),
            onSurface = Color(0xFF1B1B21),
            surfaceVariant = Color(0xFFE3E1EC),
            onSurfaceVariant = Color(0xFF46464F)
        )
    }

    MonetSeedColor.TEAL -> if (isDark) {
        darkColorScheme(
            primary = Color(0xFF80D5C3),
            onPrimary = Color(0xFF00372E),
            primaryContainer = Color(0xFF005043),
            onPrimaryContainer = Color(0xFF9CF1DE),
            secondary = Color(0xFFB1CCC5),
            onSecondary = Color(0xFF1C352F),
            secondaryContainer = Color(0xFF334B45),
            onSecondaryContainer = Color(0xFFCDE8E1),
            tertiary = Color(0xFFB2C8EC),
            onTertiary = Color(0xFF1C314E),
            tertiaryContainer = Color(0xFF334866),
            onTertiaryContainer = Color(0xFFD3E3FF),
            background = Color(0xFF0E1513),
            onBackground = Color(0xFFDEE4E1),
            surface = Color(0xFF0E1513),
            onSurface = Color(0xFFDEE4E1),
            surfaceVariant = Color(0xFF3F4945),
            onSurfaceVariant = Color(0xFFBEC9C4)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF006A5A),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFF9CF1DE),
            onPrimaryContainer = Color(0xFF00201A),
            secondary = Color(0xFF4A635C),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFCDE8E1),
            onSecondaryContainer = Color(0xFF06201B),
            tertiary = Color(0xFF4B607D),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFD3E3FF),
            onTertiaryContainer = Color(0xFF041C36),
            background = Color(0xFFF4FAF7),
            onBackground = Color(0xFF161D1B),
            surface = Color(0xFFF4FAF7),
            onSurface = Color(0xFF161D1B),
            surfaceVariant = Color(0xFFDAE5E0),
            onSurfaceVariant = Color(0xFF3F4945)
        )
    }
}

