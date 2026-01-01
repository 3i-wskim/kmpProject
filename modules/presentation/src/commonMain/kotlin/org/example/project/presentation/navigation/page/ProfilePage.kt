package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.arguments.ProfileArgument
import org.example.project.presentation.feature.profile.ProfileContract
import org.example.project.presentation.feature.profile.ProfileViewModel
import org.example.project.presentation.feature.profile.composable.ProfileScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Profile 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 * Navigation argument를 ViewModel에 파라미터로 전달합니다.
 */
@Composable
fun ProfilePage(
    argument: ProfileArgument,
    actions: MainNavigationActions,
    viewModel: ProfileViewModel = koinViewModel { parametersOf(argument.userId, argument.isEditMode) }
) {
    val state by viewModel.uiState.collectAsState()

    ProfileScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is ProfileContract.Effect.Navigation.GoBack -> actions.navigateBack()
            }
        }
    )
}