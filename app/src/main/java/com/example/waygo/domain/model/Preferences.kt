package com.example.waygo.domain.model


data class Preferences(
    val id: String,
    var notificationsEnabled: Boolean,
    var preferredLanguage: String,
    var theme: String
) {
    fun updatePreferences(newNotificationsEnabled: Boolean, newLanguage: String, newTheme: String) {
        notificationsEnabled = newNotificationsEnabled
        preferredLanguage = newLanguage
        theme = newTheme
    }

    fun getPreferences(): String {
        return "Notifications: $notificationsEnabled, Language: $preferredLanguage, Theme: $theme"
    }
}

