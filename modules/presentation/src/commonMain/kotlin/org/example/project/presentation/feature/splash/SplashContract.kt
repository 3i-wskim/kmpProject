package org.example.project.presentation.feature.splash

import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState

/**
 * Splash 화면의 MVI Contract (계약) 정의
 * State, Event, Effect를 한곳에서 관리합니다.
 */

class SplashContract {
    // 1. UI 상태 (State)
    data object State : BaseUiState

    // 2. UI 이벤트 (Event) - 사용자의 행동
    sealed interface Event : BaseUiEvent {
        data object OnAnimationEnd : Event
    }

    // 3. UI 효과 (Effect) - 단발성 이벤트
    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoToHome : Navigation
        }
    }
}