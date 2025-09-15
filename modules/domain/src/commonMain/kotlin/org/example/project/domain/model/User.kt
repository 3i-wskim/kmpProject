package org.example.project.domain.model

/**
 * Domain model representing a User.
 * This is part of the Domain layer and has no dependencies on external frameworks.
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = 0L, // 간단한 시간 표현 (테스트용)
    val updatedAt: Long = 0L
) {
    /**
     * 사용자 이름의 유효성을 검증합니다
     */
    fun isValidName(): Boolean {
        return name.isNotBlank() && name.length >= 2
    }

    /**
     * 이메일 형식의 유효성을 검증합니다
     */
    fun isValidEmail(): Boolean {
        return email.contains("@") && email.contains(".")
    }

    /**
     * 활성 사용자인지 확인합니다
     */
    fun canPerformActions(): Boolean {
        return isActive && isValidName() && isValidEmail()
    }

    /**
     * 사용자 프로필이 완성되었는지 확인합니다
     */
    fun isProfileComplete(): Boolean {
        return isValidName() && isValidEmail() && avatarUrl != null
    }
}