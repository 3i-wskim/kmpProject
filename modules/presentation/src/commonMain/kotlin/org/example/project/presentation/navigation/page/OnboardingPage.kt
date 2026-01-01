package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.feature.onboarding.OnboardingContract
import org.example.project.presentation.feature.onboarding.composable.OnboardingScreen
import org.example.project.presentation.feature.onboarding.OnboardingViewModel
import org.example.project.presentation.navigation.actions.StartNavigationActions
import org.koin.compose.viewmodel.koinViewModel

/**
 * Onboarding 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 */
@Composable
fun OnboardingPage(
    actions: StartNavigationActions,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    OnboardingScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is OnboardingContract.Effect.Navigation.GoToHome -> actions.navigateToHome()
            }
        }
    )
}