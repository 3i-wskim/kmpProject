package org.example.project.presentation.navigation.arguments

/**
 * 사용자 상세 페이지 인수
 */
data class UserDetailArgument(
    val userId: Long,
    val userName: String? = null
)

/**
 * 프로필 페이지 인수
 */
data class ProfileArgument(
    val userId: Long,
    val isEditMode: Boolean = false
)

/**
 * 일반 웹뷰 페이지 인수
 */
data class WebViewArgument(
    val url: String,
    val title: String? = null,
    val showToolbar: Boolean = true
)