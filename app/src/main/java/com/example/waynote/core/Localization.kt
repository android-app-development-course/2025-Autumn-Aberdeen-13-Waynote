package com.example.waynote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage { English, Chinese }

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.English }

fun localizedText(english: String, chinese: String, language: AppLanguage): String {
    return if (language == AppLanguage.Chinese) chinese else english
}

@Composable
fun localizedText(english: String, chinese: String): String {
    return localizedText(english, chinese, LocalAppLanguage.current)
}
