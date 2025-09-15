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
import org.example.project.presentation.screen.SplashScreen
import org.example.project.presentation.screen.OnboardingScreen
import org.example.project.presentation.screen.HomeScreen
import org.example.project.presentation.screen.UserListScreen
import org.example.project.presentation.screen.UserDetailScreen
import org.example.project.presentation.screen.ProfileScreen
import org.example.project.presentation.screen.SettingsMainScreen
import org.example.project.presentation.screen.ThemeScreen
import org.example.project.presentation.screen.LanguageScreen

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
            SplashScreen(navigationActions = startActions)
        }

        composable(Page.Start.Onboarding.route) {
            OnboardingScreen(navigationActions = startActions)
        }

        // 메인 페이지들
        composable(Page.Main.Home.route) {
            HomeScreen(navigationActions = mainActions)
        }

        composable(Page.Main.UserList.route) {
            UserListScreen(navigationActions = mainActions)
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

            UserDetailScreen(
                argument = argument,
                navigationActions = mainActions
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

            ProfileScreen(
                argument = argument,
                navigationActions = mainActions
            )
        }

        // 설정 페이지들
        composable(Page.Settings.Main.route) {
            SettingsMainScreen(navigationActions = settingsActions)
        }

        composable(Page.Settings.Theme.route) {
            ThemeScreen(navigationActions = settingsActions)
        }

        composable(Page.Settings.Language.route) {
            LanguageScreen(navigationActions = settingsActions)
        }
    }
}