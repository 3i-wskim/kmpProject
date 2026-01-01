package org.example.project.presentation.feature.userlist

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.core.BaseViewModel
import org.example.project.presentation.navigation.arguments.UserDetailArgument
class UserListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<UserListContract.State, UserListContract.Event, UserListContract.Effect>(
    initialState = UserListContract.State(isLoading = true)
) {

    init {
        loadUsers()
    }

    override fun onEvent(event: UserListContract.Event) {
        when (event) {
            is UserListContract.Event.RefreshUsers -> loadUsers()
            is UserListContract.Event.OnUserClick -> {
                val argument = UserDetailArgument(
                    userId = event.user.id.toLongOrNull() ?: 0L,
                    userName = event.user.name
                )
                setEffect { UserListContract.Effect.Navigation.GoToUserDetail(argument) }
            }
            is UserListContract.Event.OnBackClick -> {
                setEffect { UserListContract.Effect.Navigation.GoBack }
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                getUsersUseCase().collect { users ->
                    setState { copy(users = users, isLoading = false) }
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                setEffect { UserListContract.Effect.ShowError(e.message ?: "Failed to load users") }
            }
        }
    }
}