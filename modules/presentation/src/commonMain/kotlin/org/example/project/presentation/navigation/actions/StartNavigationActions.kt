package org.example.project.presentation.navigation.actions

/**
 * 시작 페이지들의 네비게이션 액션 인터페이스
 */
interface StartNavigationActions : BaseNavigationActions {
    /** 온보딩 페이지로 이동 */
    val navigateToOnboarding: () -> Unit

    /** 홈 페이지로 이동 (앱 시작 완료) */
    val navigateToHome: () -> Unit
}