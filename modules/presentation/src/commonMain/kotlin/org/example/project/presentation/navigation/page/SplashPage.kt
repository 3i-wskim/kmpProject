package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.feature.splash.SplashContract
import org.example.project.presentation.feature.splash.SplashViewModel
import org.example.project.presentation.feature.splash.composable.SplashScreen
import org.example.project.presentation.navigation.actions.StartNavigationActions
import org.koin.compose.viewmodel.koinViewModel

/**
 * Splash 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 */
@Composable
fun SplashPage(
    actions: StartNavigationActions,
    viewModel: SplashViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    SplashScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SplashContract.Effect.Navigation.GoToHome -> {
                    actions.navigateToHome()
                }
            }
        }
    )
}