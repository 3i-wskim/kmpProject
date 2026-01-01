package org.example.project.presentation.feature.user

import org.example.project.domain.model.User
import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState

/**
 * User 화면의 MVI Contract (계약) 정의
 * State, Event, Effect를 한곳에서 관리합니다.
 */
class UserContract {
    // 1. UI 상태 (State)
    data class State(
        val users: List<User> = emptyList(),
        val searchQuery: String = "",
        val isLoading: Boolean = true,
        val error: String? = null
    ) : BaseUiState {
        val isEmpty: Boolean get() = users.isEmpty() && !isLoading
        val hasError: Boolean get() = error != null
        val isSuccess: Boolean get() = !isLoading && error == null && users.isNotEmpty()
    }

    // 2. UI 이벤트 (Event) - 사용자의 행동
    sealed interface Event : BaseUiEvent {
        data class OnSearchQueryChanged(val query: String) : Event
        data class OnAddUser(val name: String, val email: String) : Event
        data object OnLoadCompleteProfiles : Event
        data object OnClearError : Event
        data object OnRefreshUsers : Event
    }

    // 3. UI 효과 (Effect) - 단발성 이벤트
    sealed interface Effect : BaseUiEffect {
        data class ShowToast(val message: String) : Effect
        data class ShowError(val message: String) : Effect
    }
}
