package org.example.project.presentation.feature.userdetail

import org.example.project.domain.model.User
import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState
import org.example.project.presentation.navigation.arguments.ProfileArgument

class UserDetailContract {
    data class State(
        val user: User? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Event : BaseUiEvent {
        data class LoadUser(val userId: Long) : Event
        data object OnBackClick : Event
        data object OnEditProfileClick : Event
    }

    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoBack : Navigation
            data class GoToProfile(val argument: ProfileArgument) : Navigation
        }
        data class ShowError(val message: String) : Effect
    }
}