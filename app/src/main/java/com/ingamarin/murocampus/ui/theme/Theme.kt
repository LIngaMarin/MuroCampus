package com.ingamarin.murocampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuroTexto
)

private val DarkColorScheme = darkColorScheme(
    primary = Azul80,
    primaryContainer = AzulContenedorOscuro
)

/** Tema azul de MuroCampus (sin color dinamico, para que se vea igual en todos los celulares). */
@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
