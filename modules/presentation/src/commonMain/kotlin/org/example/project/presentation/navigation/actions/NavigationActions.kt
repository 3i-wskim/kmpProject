package org.example.project.presentation.navigation.actions

/**
 * 기본 네비게이션 액션 인터페이스
 * - 모든 네비게이션 액션이 공통으로 가져야 할 기본 기능들을 정의
 */
interface BaseNavigationActions {
    /** 뒤로 가기 */
    val navigateBack: () -> Unit

    /** 현재 화면 종료 */
    val finish: () -> Unit
}