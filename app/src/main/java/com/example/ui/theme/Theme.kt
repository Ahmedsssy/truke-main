package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ProfessionalDarkColorScheme = darkColorScheme(
    primary = LightBlueContainer,
    secondary = ActiveBlue,
    background = MidnightBlue,
    surface = HighContrastBlue,
    onPrimary = MidnightBlue,
    onSecondary = PureWhite,
    onBackground = SlateBackground,
    onSurface = SlateBackground,
    primaryContainer = HighContrastBlue,
    onPrimaryContainer = LightBlueContainer,
    error = LightRedContainer,
    errorContainer = DeepRed,
    onErrorContainer = LightRedContainer,
    surfaceVariant = HighContrastBlue,
    outline = BorderGray
)

private val ProfessionalLightColorScheme = lightColorScheme(
    primary = MidnightBlue,
    secondary = ActiveBlue,
    background = SlateBackground,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onBackground = JetBlack,
    onSurface = JetBlack,
    primaryContainer = LightBlueContainer,
    onPrimaryContainer = MidnightBlue,
    error = DeepRed,
    errorContainer = LightRedContainer,
    onErrorContainer = DeepRed,
    surfaceVariant = NavigationGray,
    outline = BorderGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Enforce light theme by default for high visual crispness
    dynamicColor: Boolean = false, // Set to false to fully express the custom corporate branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ProfessionalDarkColorScheme else ProfessionalLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
