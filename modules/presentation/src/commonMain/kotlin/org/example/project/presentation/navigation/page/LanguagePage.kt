package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.navigation.actions.SettingsNavigationActions
import org.example.project.presentation.feature.settings.SettingsContract
import org.example.project.presentation.feature.settings.SettingsViewModel
import org.example.project.presentation.feature.settings.composable.LanguageScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Language 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 */
@Composable
fun LanguagePage(
    actions: SettingsNavigationActions,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LanguageScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SettingsContract.Effect.Navigation.GoBack -> actions.navigateBack()
                is SettingsContract.Effect.Navigation.GoToLanguage -> {} // Already on language page
                is SettingsContract.Effect.Navigation.GoToTheme -> actions.navigateToTheme()
            }
        }
    )
}