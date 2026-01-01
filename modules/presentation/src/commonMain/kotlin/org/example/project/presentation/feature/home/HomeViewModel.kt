package org.example.project.presentation.feature.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.core.BaseViewModel
import org.example.project.presentation.navigation.arguments.ProfileArgument
class HomeViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    initialState = HomeContract.State()
) {

    init {
        loadData()
    }

    override fun onEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.RefreshUsers -> loadData()
            
            is HomeContract.Event.OnClickUserList -> {
                setEffect { HomeContract.Effect.Navigation.GoToUserList }
            }
            
            is HomeContract.Event.OnClickSettings -> {
                setEffect { HomeContract.Effect.Navigation.GoToSettings }
            }
            
            is HomeContract.Event.OnClickProfile -> {
                setEffect { 
                    HomeContract.Effect.Navigation.GoToProfile(
                        ProfileArgument(userId = event.userId, isEditMode = false)
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                // 예시: 간단히 사용자 수만 체크하거나 필요한 초기 데이터 로드
                // val users = getUsersUseCase.execute()
                setState { copy(isLoading = false) }
            } catch (e: Exception) {
                setState { copy(isLoading = false, error = e.message) }
                setEffect { HomeContract.Effect.ShowError(e.message ?: "Error loading data") }
            }
        }
    }
}