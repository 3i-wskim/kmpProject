package org.example.project.presentation.navigation.impl

import androidx.navigation.NavController
import org.example.project.presentation.navigation.Page
import org.example.project.presentation.navigation.actions.StartNavigationActions
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.actions.SettingsNavigationActions
import org.example.project.presentation.navigation.arguments.UserDetailArgument
import org.example.project.presentation.navigation.arguments.ProfileArgument

/**
 * 시작 페이지 네비게이션 액션 구현체
 */
class StartNavigationActionsImpl(
    private val navController: NavController
) : StartNavigationActions {

    override val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    override val finish: () -> Unit = {
        navController.popBackStack()
    }

    override val navigateToOnboarding: () -> Unit = {
        navController.navigate(Page.Start.Onboarding.route)
    }

    override val navigateToHome: () -> Unit = {
        navController.navigate(Page.Main.Home.route) {
            // 스플래시/온보딩 화면들을 백스택에서 제거
            popUpTo(Page.START_DESTINATION) {
                inclusive = true
            }
        }
    }
}

/**
 * 메인 페이지 네비게이션 액션 구현체
 */
class MainNavigationActionsImpl(
    private val navController: NavController
) : MainNavigationActions {

    override val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    override val finish: () -> Unit = {
        navController.popBackStack()
    }

    override val navigateToUserList: () -> Unit = {
        navController.navigate(Page.Main.UserList.route)
    }

    override val navigateToUserDetail: (UserDetailArgument) -> Unit = { argument ->
        // 간단한 방식: userId와 userName을 URL 파라미터로 전달
        val route = "${Page.Main.UserDetail.route}/${argument.userId}" +
                (argument.userName?.let { "?name=$it" } ?: "")
        navController.navigate(route)
    }

    override val navigateToProfile: (ProfileArgument) -> Unit = { argument ->
        // 간단한 방식: userId와 isEditMode를 URL 파라미터로 전달  
        val route = "${Page.Main.Profile.route}/${argument.userId}/${argument.isEditMode}"
        navController.navigate(route)
    }

    override val navigateToSettings: () -> Unit = {
        navController.navigate(Page.Settings.Main.route)
    }
}

/**
 * 설정 페이지 네비게이션 액션 구현체
 */
class SettingsNavigationActionsImpl(
    private val navController: NavController
) : SettingsNavigationActions {

    override val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    override val finish: () -> Unit = {
        navController.popBackStack()
    }

    override val navigateToTheme: () -> Unit = {
        navController.navigate(Page.Settings.Theme.route)
    }

    override val navigateToLanguage: () -> Unit = {
        navController.navigate(Page.Settings.Language.route)
    }

    override val navigateToHome: () -> Unit = {
        navController.navigate(Page.Main.Home.route) {
            popUpTo(Page.Settings.Main.route) {
                inclusive = true
            }
        }
    }
}