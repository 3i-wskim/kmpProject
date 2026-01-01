package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.arguments.UserDetailArgument
import org.example.project.presentation.feature.userdetail.UserDetailContract
import org.example.project.presentation.feature.userdetail.UserDetailViewModel
import org.example.project.presentation.feature.userdetail.composable.UserDetailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * UserDetail 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 * Navigation argument를 ViewModel에 파라미터로 전달합니다.
 */
@Composable
fun UserDetailPage(
    argument: UserDetailArgument,
    actions: MainNavigationActions,
    viewModel: UserDetailViewModel = koinViewModel { parametersOf(argument.userId) }
) {
    val state by viewModel.uiState.collectAsState()

    UserDetailScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is UserDetailContract.Effect.Navigation.GoBack -> actions.navigateBack()
                is UserDetailContract.Effect.Navigation.GoToProfile -> actions.navigateToProfile(navigationEffect.argument)
            }
        }
    )
}