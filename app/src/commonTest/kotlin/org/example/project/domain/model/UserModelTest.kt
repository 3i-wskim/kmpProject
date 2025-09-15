package org.example.project.domain.model

import org.example.project.core.BaseTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals

/**
 * User 도메인 모델 테스트
 *
 * 비즈니스 로직과 검증 규칙을 테스트합니다.
 */
class UserModelTest : BaseTest() {

    override fun onStart() {
        println("User 도메인 모델 테스트 시작")
    }

    override fun onStop() {
        println("User 도메인 모델 테스트 완료")
    }

    @Test
    fun t10_유효한_사용자_이름_검증() {
        // Given
        val validNames = listOf("김철수", "John", "테스트", "AB")

        // When
        validNames.forEach { name ->
            val user = User(id = "test", name = name, email = "test@test.com")
        }

        // Then
        then("모든 이름이 유효해야 함") {
            validNames.forEach { name ->
                val user = User(id = "test", name = name, email = "test@test.com")
                assertTrue(user.isValidName(), "$name should be valid")
            }
        }
    }

    @Test
    fun t11_무효한_사용자_이름_검증() {
        // Given
        val invalidNames = listOf("", " ", "A")

        // When
        invalidNames.forEach { name ->
            val user = User(id = "test", name = name, email = "test@test.com")
        }

        // Then
        then("모든 이름이 무효해야 함") {
            invalidNames.forEach { name ->
                val user = User(id = "test", name = name, email = "test@test.com")
                assertFalse(user.isValidName(), "$name should be invalid")
            }
        }
    }

    @Test
    fun t20_유효한_이메일_검증() {
        // Given
        val validEmails = listOf(
            "test@example.com",
            "user.name@domain.co.kr",
            "admin@test.org"
        )

        // When
        validEmails.forEach { email ->
            val user = User(id = "test", name = "테스트", email = email)
        }

        // Then
        then("모든 이메일이 유효해야 함") {
            validEmails.forEach { email ->
                val user = User(id = "test", name = "테스트", email = email)
                assertTrue(user.isValidEmail(), "$email should be valid")
            }
        }
    }

    @Test
    fun t21_무효한_이메일_검증() {
        // Given
        val invalidEmails = listOf(
            "invalid",
            "test@",
            "@domain.com",
            "test.com"
        )

        // When
        invalidEmails.forEach { email ->
            val user = User(id = "test", name = "테스트", email = email)
        }

        // Then
        then("모든 이메일이 무효해야 함") {
            invalidEmails.forEach { email ->
                val user = User(id = "test", name = "테스트", email = email)
                assertFalse(user.isValidEmail(), "$email should be invalid")
            }
        }
    }

    @Test
    fun t30_사용자_액션_수행_가능성_검증() {
        // Given
        val user = User(
            id = "test",
            name = "김철수",
            email = "kim@test.com",
            isActive = true
        )

        // When
        val canPerform = user.canPerformActions()

        // Then
        then("액션을 수행할 수 있어야 함") {
            assertTrue(user.canPerformActions())
        }
    }

    @Test
    fun t31_비활성_사용자_액션_수행_불가_검증() {
        // Given
        val user = User(
            id = "test",
            name = "김철수",
            email = "kim@test.com",
            isActive = false
        )

        // When
        val canPerform = user.canPerformActions()

        // Then
        then("액션을 수행할 수 없어야 함") {
            assertFalse(user.canPerformActions())
        }
    }

    @Test
    fun t40_완성된_프로필_검증() {
        // Given
        val user = User(
            id = "test",
            name = "김철수",
            email = "kim@test.com",
            avatarUrl = "https://example.com/avatar.jpg",
            isActive = true
        )

        // When
        val isComplete = user.isProfileComplete()

        // Then
        then("프로필이 완성되었어야 함") {
            assertTrue(user.isProfileComplete())
        }
    }

    @Test
    fun t41_미완성_프로필_검증() {
        // Given
        val user = User(
            id = "test",
            name = "김철수",
            email = "kim@test.com",
            avatarUrl = null,
            isActive = true
        )

        // When
        val isComplete = user.isProfileComplete()

        // Then
        then("프로필이 미완성이어야 함") {
            assertFalse(user.isProfileComplete())
        }
    }

    @Test
    fun t50_사용자_데이터_클래스_속성_검증() {
        // Given
        val user = User(
            id = "user123",
            name = "테스트 사용자",
            email = "test@example.com",
            avatarUrl = "https://example.com/avatar.jpg",
            isActive = true,
            createdAt = 1000L,
            updatedAt = 2000L
        )

        // When

        // Then
        then("모든 속성이 정확해야 함") {
            assertAll(
                { assertEquals("user123", user.id) },
                { assertEquals("테스트 사용자", user.name) },
                { assertEquals("test@example.com", user.email) },
                { assertEquals("https://example.com/avatar.jpg", user.avatarUrl) },
                { assertTrue(user.isActive) },
                { assertEquals(1000L, user.createdAt) },
                { assertEquals(2000L, user.updatedAt) }
            )
        }
    }
}
