package com.example.waynote.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = Color.White,
    primaryContainer = PowderBlue,
    onPrimaryContainer = MidnightBlue,
    secondary = AquaAccent,
    onSecondary = MidnightBlue,
    tertiary = DeepBlue,
    onTertiary = Color.White,
    background = MistBlue,
    onBackground = MidnightBlue,
    surface = Color.White,
    onSurface = MidnightBlue,
    surfaceVariant = PowderBlue,
    onSurfaceVariant = DeepBlue,
    outline = DeepBlue.copy(alpha = 0.3f)
)

@Composable
fun WaynoteTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
