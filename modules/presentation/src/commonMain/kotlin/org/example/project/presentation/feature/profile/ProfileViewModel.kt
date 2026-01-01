package org.example.project.presentation.feature.profile

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.core.BaseViewModel
/**
 * Profile ViewModel
 * @param userId 사용자 ID - Navigation에서 전달받음
 * @param isEditMode 편집 모드 여부 - Navigation에서 전달받음
 * @param getUsersUseCase 사용자 조회 UseCase - Koin이 자동 주입
 */
class ProfileViewModel(
    private val userId: Long,
    private val isEditMode: Boolean,
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<ProfileContract.State, ProfileContract.Event, ProfileContract.Effect>(
    initialState = ProfileContract.State(isLoading = true, isEditMode = isEditMode)
) {

    init {
        // ViewModel 생성 시 자동으로 데이터 로드
        loadProfile(userId, isEditMode)
    }

    override fun onEvent(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.LoadProfile -> loadProfile(event.userId, event.isEditMode)
            is ProfileContract.Event.OnBackClick -> {
                setEffect { ProfileContract.Effect.Navigation.GoBack }
            }
            is ProfileContract.Event.OnSaveClick -> {
                // TODO: 실제 저장 로직 구현 (UseCase 호출)
                setEffect { ProfileContract.Effect.ShowToast("저장되었습니다.") }
                setEffect { ProfileContract.Effect.Navigation.GoBack }
            }
        }
    }

    private fun loadProfile(userId: Long, isEditMode: Boolean) {
        viewModelScope.launch {
            setState { copy(isLoading = true, isEditMode = isEditMode) }
            try {
                // 실제 유스케이스 연동
                val user = getUsersUseCase.getUserById(userId.toString())
                
                if (user != null) {
                    setState { copy(user = user, isLoading = false) }
                } else {
                    setState { copy(isLoading = false, error = "사용자 정보를 찾을 수 없습니다.") }
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                setEffect { ProfileContract.Effect.ShowError(e.message ?: "Load failed") }
            }
        }
    }
}