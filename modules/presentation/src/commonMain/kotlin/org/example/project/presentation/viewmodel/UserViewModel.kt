package org.example.project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.domain.model.User
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase

/**
 * ViewModel for User management screens.
 * Depends only on Domain layer use cases, maintaining clean architecture separation.
 */
class UserViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            combine(
                searchQuery,
                if (searchQuery.value.isEmpty()) {
                    getUsersUseCase()
                } else {
                    getUsersUseCase.searchByName(searchQuery.value)
                }
            ) { query, users ->
                _uiState.value = _uiState.value.copy(
                    users = users,
                    isLoading = false,
                    error = null
                )
            }.collect()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        observeUsers()
    }

    fun addUser(name: String, email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newUser = User(
                id = generateUserId(),
                name = name,
                email = email
            )

            addUserUseCase(newUser)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    // Refresh users list
                    observeUsers()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun generateUserId(): String {
        // WASM-compatible ID generation using random number
        return kotlin.random.Random.nextLong(100000, 999999).toString()
    }
}

/**
 * UI State for User screens
 */
data class UserUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)