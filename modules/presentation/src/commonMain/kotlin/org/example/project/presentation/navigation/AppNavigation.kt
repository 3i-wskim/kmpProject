package org.example.project.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import org.example.project.presentation.navigation.impl.StartNavigationActionsImpl
import org.example.project.presentation.navigation.impl.MainNavigationActionsImpl
import org.example.project.presentation.navigation.impl.SettingsNavigationActionsImpl
import org.example.project.presentation.navigation.arguments.UserDetailArgument
import org.example.project.presentation.navigation.arguments.ProfileArgument
import org.example.project.presentation.navigation.page.SplashPage
import org.example.project.presentation.navigation.page.OnboardingPage
import org.example.project.presentation.navigation.page.HomePage
import org.example.project.presentation.navigation.page.UserListPage
import org.example.project.presentation.navigation.page.UserDetailPage
import org.example.project.presentation.navigation.page.ProfilePage
import org.example.project.presentation.navigation.page.SettingsMainPage
import org.example.project.presentation.navigation.page.ThemePage
import org.example.project.presentation.navigation.page.LanguagePage

/**
 * 앱의 메인 네비게이션 컴포넌트
 * - 모든 페이지들을 관리하고 라우팅을 처리
 * - 각 페이지별로 적절한 네비게이션 액션을 제공
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // 각 그룹별 네비게이션 액션 구현체 생성
    val startActions = remember { StartNavigationActionsImpl(navController) }
    val mainActions = remember { MainNavigationActionsImpl(navController) }
    val settingsActions = remember { SettingsNavigationActionsImpl(navController) }

    NavHost(
        navController = navController,
        startDestination = Page.START_DESTINATION
    ) {
        // 시작 페이지들
        composable(Page.Start.Splash.route) {
            SplashPage(actions = startActions)
        }

        composable(Page.Start.Onboarding.route) {
            OnboardingPage(actions = startActions)
        }

        // 메인 페이지들
        composable(Page.Main.Home.route) { backStackEntry ->
            HomePage(
                actions = mainActions,
                navBackStackEntry = backStackEntry
            )
        }

        composable(Page.Main.UserList.route) {
            UserListPage(actions = mainActions)
        }

        // 사용자 상세 페이지 (URL 파라미터 방식)
        composable(
            route = "${Page.Main.UserDetail.route}/{userId}?name={name}",
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            val userName = backStackEntry.arguments?.getString("name")
            val argument = UserDetailArgument(userId = userId, userName = userName)

            UserDetailPage(
                argument = argument,
                actions = mainActions
            )
        }

        // 프로필 페이지 (URL 파라미터 방식)
        composable(
            route = "${Page.Main.Profile.route}/{userId}/{isEditMode}",
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("isEditMode") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            val isEditMode = backStackEntry.arguments?.getBoolean("isEditMode") ?: false
            val argument = ProfileArgument(userId = userId, isEditMode = isEditMode)

            ProfilePage(
                argument = argument,
                actions = mainActions
            )
        }

        // 설정 페이지들
        composable(Page.Settings.Main.route) {
            SettingsMainPage(actions = settingsActions)
        }

        composable(Page.Settings.Theme.route) {
            ThemePage(actions = settingsActions)
        }

        composable(Page.Settings.Language.route) {
            LanguagePage(actions = settingsActions)
        }
    }
}