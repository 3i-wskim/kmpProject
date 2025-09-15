package org.example.project.presentation.navigation.actions

import org.example.project.presentation.navigation.arguments.UserDetailArgument
import org.example.project.presentation.navigation.arguments.ProfileArgument

/**
 * 메인 페이지들의 네비게이션 액션 인터페이스
 */
interface MainNavigationActions : BaseNavigationActions {
    /** 사용자 목록 페이지로 이동 */
    val navigateToUserList: () -> Unit

    /** 사용자 상세 페이지로 이동 */
    val navigateToUserDetail: (UserDetailArgument) -> Unit

    /** 프로필 페이지로 이동 */
    val navigateToProfile: (ProfileArgument) -> Unit

    /** 설정 페이지로 이동 */
    val navigateToSettings: () -> Unit
}