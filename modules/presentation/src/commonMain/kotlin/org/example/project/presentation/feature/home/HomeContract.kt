package org.example.project.presentation.feature.home

import org.example.project.domain.model.User
import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState
import org.example.project.presentation.navigation.arguments.ProfileArgument

/**
 * Home 화면의 MVI Contract (계약) 정의
 * State, Event, Effect를 한곳에서 관리합니다.
 */

class HomeContract {
    // 1. UI 상태 (State)
    data class State(
        val users: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    // 2. UI 이벤트 (Event) - 사용자의 행동
    sealed interface Event : BaseUiEvent {
        data object RefreshUsers : Event
        data object OnClickUserList : Event
        data object OnClickSettings : Event
        data class OnClickProfile(val userId: Long) : Event
    }

    // 3. UI 효과 (Effect) - 단발성 이벤트
    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoToUserList : Navigation
            data object GoToSettings : Navigation
            data class GoToProfile(val argument: ProfileArgument) : Navigation
        }
        
        data class ShowToast(val message: String) : Effect
        data class ShowError(val message: String) : Effect
    }
}