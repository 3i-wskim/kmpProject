package org.example.project.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.presentation.core.BaseViewModel
import org.example.project.presentation.settings.SettingsStorage
class SettingsViewModel(
    private val storage: SettingsStorage
) : BaseViewModel<SettingsContract.State, SettingsContract.Event, SettingsContract.Effect>(
    initialState = SettingsContract.State()
) {

    init {
        loadSettings()
    }

    override fun onEvent(event: SettingsContract.Event) {
        when (event) {
            is SettingsContract.Event.OnThemeChanged -> setTheme(event.mode)
            is SettingsContract.Event.OnLanguageChanged -> setLanguage(event.code)
            is SettingsContract.Event.OnThemeClick -> {
                setEffect { SettingsContract.Effect.Navigation.GoToTheme }
            }
            is SettingsContract.Event.OnLanguageClick -> {
                setEffect { SettingsContract.Effect.Navigation.GoToLanguage }
            }
            is SettingsContract.Event.OnBackClick -> {
                setEffect { SettingsContract.Effect.Navigation.GoBack }
            }
            is SettingsContract.Event.ClearError -> {
                setState { copy(error = null) }
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            
            val themeResult = storage.getTheme()
            val languageResult = storage.getLanguage()
            
            if (themeResult.isSuccess && languageResult.isSuccess) {
                setState {
                    copy(
                        themeMode = themeResult.getOrThrow(),
                        languageCode = languageResult.getOrThrow(),
                        isLoading = false
                    )
                }
            } else {
                setState { copy(isLoading = false, error = "설정을 불러오는데 실패했습니다.") }
            }
        }
    }

    private fun setTheme(mode: org.example.project.presentation.settings.ThemeMode) {
        viewModelScope.launch {
            storage.setTheme(mode)
                .onSuccess { 
                    setState { copy(themeMode = mode) }
                }
                .onFailure { 
                    setEffect { SettingsContract.Effect.ShowError(it.message ?: "테마 변경 실패") }
                }
        }
    }

    private fun setLanguage(code: org.example.project.presentation.settings.LanguageCode) {
        viewModelScope.launch {
            storage.setLanguage(code)
                .onSuccess { 
                    setState { copy(languageCode = code) }
                }
                .onFailure { 
                    setEffect { SettingsContract.Effect.ShowError(it.message ?: "언어 변경 실패") }
                }
        }
    }
}