package com.example.themechangevideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class ViewModel(private val dataStore: DataStore) : ViewModel() {

    val themeState: StateFlow<ThemeState> = dataStore.getCurrentTheme
        .map { isDark -> ThemeState.Loaded(isDark) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeState.Loading
        )

    fun setNewTheme() = viewModelScope.launch {
        val currentState = themeState.value
        if (currentState is ThemeState.Loaded) {
            dataStore.setNewTheme(isDarkTheme = !currentState.isDarkTheme)
        }
    }
    /*
        private fun getCurrentTheme() = viewModelScope.launch {
            dataStore.getCurrentTheme.collectLatest { theme ->
                _themeState.update { it.copy(theme) }
            }
        }*/
}

sealed class ThemeState {
    data object Loading : ThemeState()
    data class Loaded(val isDarkTheme: Boolean) : ThemeState()
}