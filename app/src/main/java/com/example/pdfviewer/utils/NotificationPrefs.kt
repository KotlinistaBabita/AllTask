package com.example.pdfviewer.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object NotificationPrefs {
    private val NOTIFICATION_ENABLED = booleanPreferencesKey("notifications_enabled")
    private val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")


    fun isDarkModeEnabled(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[DARK_MODE_ENABLED] ?: false }

    suspend fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE_ENABLED] = enabled }
    }


    suspend fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATION_ENABLED] = enabled
        }
    }

    fun isNotificationEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[NOTIFICATION_ENABLED] ?: true
        }
    }
}
