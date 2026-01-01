package org.example.project.presentation.feature.profile

import org.example.project.domain.model.User
import org.example.project.presentation.core.BaseUiEffect
import org.example.project.presentation.core.BaseUiEvent
import org.example.project.presentation.core.BaseUiState

class ProfileContract {
    data class State(
        val user: User? = null,
        val isEditMode: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Event : BaseUiEvent {
        data class LoadProfile(val userId: Long, val isEditMode: Boolean) : Event
        data object OnBackClick : Event
        data object OnSaveClick : Event
    }

    sealed interface Effect : BaseUiEffect {
        sealed interface Navigation : Effect {
            data object GoBack : Navigation
        }
        data class ShowToast(val message: String) : Effect
        data class ShowError(val message: String) : Effect
    }
}