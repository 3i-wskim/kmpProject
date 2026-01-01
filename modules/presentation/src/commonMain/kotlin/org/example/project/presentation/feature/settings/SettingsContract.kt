package org.example.project.presentation.feature.settings

import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState
import org.example.project.presentation.settings.LanguageCode
import org.example.project.presentation.settings.ThemeMode

class SettingsContract {
    data class State(
        val themeMode: ThemeMode = ThemeMode.SYSTEM,
        val languageCode: LanguageCode = LanguageCode.KO,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Event : BaseUiEvent {
        data class OnThemeChanged(val mode: ThemeMode) : Event
        data class OnLanguageChanged(val code: LanguageCode) : Event
        data object OnThemeClick : Event
        data object OnLanguageClick : Event
        data object OnBackClick : Event
        data object ClearError : Event
    }

    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoBack : Navigation
            data object GoToTheme : Navigation
            data object GoToLanguage : Navigation
        }
        data class ShowError(val message: String) : Effect
    }
}