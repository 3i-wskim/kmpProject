package org.example.project.presentation.feature.onboarding

import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState

class OnboardingContract {
    data object State : BaseUiState

    sealed interface Event : BaseUiEvent {
        data object OnStartClick : Event
    }

    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoToHome : Navigation
        }
    }
}