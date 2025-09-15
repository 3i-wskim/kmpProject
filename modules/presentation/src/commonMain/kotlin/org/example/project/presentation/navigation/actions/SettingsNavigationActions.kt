package org.example.project.presentation.navigation.actions

/**
 * 설정 페이지들의 네비게이션 액션 인터페이스
 */
interface SettingsNavigationActions : BaseNavigationActions {
    /** 테마 설정 페이지로 이동 */
    val navigateToTheme: () -> Unit

    /** 언어 설정 페이지로 이동 */
    val navigateToLanguage: () -> Unit

    /** 홈으로 돌아가기 */
    val navigateToHome: () -> Unit
}