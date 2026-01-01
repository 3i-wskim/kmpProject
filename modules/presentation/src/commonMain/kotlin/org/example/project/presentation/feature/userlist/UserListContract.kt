package org.example.project.presentation.feature.userlist

import org.example.project.domain.model.User
import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState
import org.example.project.presentation.navigation.arguments.UserDetailArgument

class UserListContract {
    data class State(
        val users: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Event : BaseUiEvent {
        data object RefreshUsers : Event
        data class OnUserClick(val user: User) : Event
        data object OnBackClick : Event
    }

    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoBack : Navigation
            data class GoToUserDetail(val argument: UserDetailArgument) : Navigation
        }
        data class ShowError(val message: String) : Effect
    }
}