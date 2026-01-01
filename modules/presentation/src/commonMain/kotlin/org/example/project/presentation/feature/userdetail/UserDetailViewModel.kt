package org.example.project.presentation.feature.userdetail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.core.BaseViewModel
import org.example.project.presentation.navigation.arguments.ProfileArgument
/**
 * UserDetail ViewModel
 * @param userId 사용자 ID - Navigation에서 전달받음
 * @param getUsersUseCase 사용자 조회 UseCase - Koin이 자동 주입
 */
class UserDetailViewModel(
    private val userId: Long,
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<UserDetailContract.State, UserDetailContract.Event, UserDetailContract.Effect>(
    initialState = UserDetailContract.State(isLoading = true)
) {

    init {
        // ViewModel 생성 시 자동으로 데이터 로드
        loadUser(userId)
    }

    override fun onEvent(event: UserDetailContract.Event) {
        when (event) {
            is UserDetailContract.Event.LoadUser -> loadUser(event.userId)
            is UserDetailContract.Event.OnBackClick -> {
                setEffect { UserDetailContract.Effect.Navigation.GoBack }
            }
            is UserDetailContract.Event.OnEditProfileClick -> {
                val user = uiState.value.user
                if (user != null) {
                    val userId = user.id.toLongOrNull() ?: 0L
                    setEffect { 
                        UserDetailContract.Effect.Navigation.GoToProfile(
                            ProfileArgument(userId = userId, isEditMode = true)
                        ) 
                    }
                }
            }
        }
    }

    private fun loadUser(userId: Long) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                // 실제로는 getUserById 유스케이스가 필요하지만, 
                // 예제에서는 getUsersUseCase에서 필터링하거나 임시 처리
                val user = getUsersUseCase.getUserById(userId.toString())
                
                if (user != null) {
                    setState { copy(user = user, isLoading = false) }
                } else {
                    setState { copy(isLoading = false, error = "사용자를 찾을 수 없습니다.") }
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                setEffect { UserDetailContract.Effect.ShowError(e.message ?: "Load failed") }
            }
        }
    }
}