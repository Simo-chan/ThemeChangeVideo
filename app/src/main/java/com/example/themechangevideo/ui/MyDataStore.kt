package com.example.themechangevideo.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyDataStore(val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    suspend fun setNewTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_PREFERENCES_KEY] = isDarkTheme
        }
    }

    val getCurrentTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[THEME_PREFERENCES_KEY] == true
    }

    private companion object {
        const val DATA_STORE_NAME = "user_settings"
        val THEME_PREFERENCES_KEY = booleanPreferencesKey("is_dark_theme")
    }
}