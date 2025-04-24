package com.example.themechangevideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExampleViewModel(private val dataStore: DataStore) : ViewModel() {

    private val _themeState = MutableStateFlow(ThemeState())
    val themeState = _themeState.asStateFlow()

    init {
        getCurrentTheme()
    }

    fun setNewTheme() = viewModelScope.launch {
        val currentState = themeState.value
        if (!currentState.isLoading) {
            dataStore.setNewTheme(isDarkTheme = !currentState.isDarkTheme)
        }
    }

    private fun getCurrentTheme() = viewModelScope.launch {
        _themeState.update { it.copy(isLoading = true) }

        dataStore.getCurrentTheme.collectLatest { theme ->
            _themeState.update { it.copy(isLoading = false, isDarkTheme = theme) }
        }
    }
}


