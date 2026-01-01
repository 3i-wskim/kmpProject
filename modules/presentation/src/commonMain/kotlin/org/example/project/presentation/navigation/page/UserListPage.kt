package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.feature.userlist.UserListContract
import org.example.project.presentation.feature.userlist.UserListViewModel
import org.example.project.presentation.feature.userlist.composable.UserListScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * UserList 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 */
@Composable
fun UserListPage(
    actions: MainNavigationActions,
    viewModel: UserListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    UserListScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is UserListContract.Effect.Navigation.GoBack -> actions.navigateBack()
                is UserListContract.Effect.Navigation.GoToUserDetail -> actions.navigateToUserDetail(navigationEffect.argument)
            }
        }
    )
}