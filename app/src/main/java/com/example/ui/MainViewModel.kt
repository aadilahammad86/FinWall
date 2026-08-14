package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.navigation.Screen
import com.example.ui.theme.MonetSeedColor
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainUiState(
    val currentScreen: Screen = Screen.Home,
    val previousScreenIndex: Int = 0,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isDynamicMonetEnabled: Boolean = false,
    val selectedSeedColor: MonetSeedColor = MonetSeedColor.PURPLE,
    val isBouncyMotion: Boolean = true
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: Screen) {
        _uiState.update { currentState ->
            if (currentState.currentScreen == screen) currentState
            else currentState.copy(
                previousScreenIndex = currentState.currentScreen.index,
                currentScreen = screen
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setDynamicMonetEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isDynamicMonetEnabled = enabled) }
    }

    fun setSeedColor(seed: MonetSeedColor) {
        _uiState.update { it.copy(selectedSeedColor = seed) }
    }

    fun toggleBouncyMotion() {
        _uiState.update { it.copy(isBouncyMotion = !it.isBouncyMotion) }
    }
}
