package org.example.project.presentation.navigation.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import org.example.project.presentation.feature.home.HomeContract
import org.example.project.presentation.feature.home.HomeViewModel
import org.example.project.presentation.feature.home.composable.HomeScreen
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home 페이지
 * Koin을 사용하여 ViewModel을 자동 주입합니다.
 */
@Composable
fun HomePage(
    actions: MainNavigationActions, 
    navBackStackEntry: NavBackStackEntry,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    HomeScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.onEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is HomeContract.Effect.Navigation.GoToUserList -> {
                    actions.navigateToUserList()
                }
                is HomeContract.Effect.Navigation.GoToSettings -> {
                    actions.navigateToSettings()
                }
                is HomeContract.Effect.Navigation.GoToProfile -> {
                    actions.navigateToProfile(navigationEffect.argument)
                }
            }
        }
    )
}