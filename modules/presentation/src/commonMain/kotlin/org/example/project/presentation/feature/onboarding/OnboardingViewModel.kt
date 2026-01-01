package org.example.project.presentation.feature.onboarding

import org.example.project.presentation.core.BaseViewModel
class OnboardingViewModel : BaseViewModel<OnboardingContract.State, OnboardingContract.Event, OnboardingContract.Effect>(
    initialState = OnboardingContract.State
) {
    override fun onEvent(event: OnboardingContract.Event) {
        when (event) {
            is OnboardingContract.Event.OnStartClick -> {
                setEffect { OnboardingContract.Effect.Navigation.GoToHome }
            }
        }
    }
}