package org.example.project.presentation.feature.user

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.domain.model.User
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.core.BaseViewModel
/**
 * User 화면을 위한 ViewModel (MVI 패턴)
 * Domain layer use cases에만 의존하여 클린 아키텍처 분리를 유지합니다.
 */
class UserViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase
) : BaseViewModel<UserContract.State, UserContract.Event, UserContract.Effect>(
    initialState = UserContract.State()
) {

    init {
        observeUsers()
    }

    override fun onEvent(event: UserContract.Event) {
        when (event) {
            is UserContract.Event.OnSearchQueryChanged -> {
                setState { copy(searchQuery = event.query) }
                observeUsers()
            }
            
            is UserContract.Event.OnAddUser -> {
                addUser(event.name, event.email)
            }
            
            is UserContract.Event.OnLoadCompleteProfiles -> {
                loadCompleteProfiles()
            }
            
            is UserContract.Event.OnClearError -> {
                setState { copy(error = null) }
            }
            
            is UserContract.Event.OnRefreshUsers -> {
                observeUsers()
            }
        }
    }

    private fun observeUsers() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            
            try {
                val flow = if (uiState.value.searchQuery.isEmpty()) {
                    getUsersUseCase()
                } else {
                    getUsersUseCase.searchByName(uiState.value.searchQuery)
                }
                
                flow.collect { users ->
                    setState { 
                        copy(
                            users = users,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                setState { 
                    copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                setEffect { UserContract.Effect.ShowError(e.message ?: "사용자 목록 로드 실패") }
            }
        }
    }

    private fun addUser(name: String, email: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val newUser = User(
                id = generateUserId(),
                name = name,
                email = email
            )

            addUserUseCase(newUser)
                .onSuccess { 
                    setState { copy(isLoading = false, error = null) }
                    setEffect { UserContract.Effect.ShowToast("사용자가 추가되었습니다.") }
                    // 목록 새로고침
                    observeUsers()
                }
                .onFailure { error ->
                    setState { 
                        copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                    setEffect { UserContract.Effect.ShowError(error.message ?: "사용자 추가 실패") }
                }
        }
    }

    private fun loadCompleteProfiles() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            
            try {
                getUsersUseCase.getCompleteProfiles()
                    .collect { users ->
                        setState { 
                            copy(
                                users = users,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                setState { 
                    copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                setEffect { UserContract.Effect.ShowError(e.message ?: "프로필 로드 실패") }
            }
        }
    }

    private fun generateUserId(): String {
        // WASM 호환 ID 생성 (random 사용)
        return kotlin.random.Random.nextLong(100000, 999999).toString()
    }
}
