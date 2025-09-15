package org.example.project.mocks

import org.example.project.core.ApplicationState

/**
 * 테스트에서 사용할 Mock 데이터와 객체들
 *
 * 실제 객체 대신 사용할 수 있는 테스트용 데이터를 제공합니다.
 * Mock 객체는 최소한으로 사용하고 가능한 경우 실제 객체를 사용하는 것이 권장됩니다.
 */
object TestMocks {

    // 기본 테스트 상수값들
    const val TEST_EMAIL = "test@example.com"
    const val TEST_USERNAME = "testuser"
    const val TEST_PASSWORD = "testPassword"
    const val TEST_USER_ID = "user123"
    const val TEST_PROJECT_NAME = "TestProject"
    const val TEST_VERSION = "1.0.0"
    const val TEST_DELAY_MS = 100L

    /**
     * 테스트용 사용자 데이터 클래스
     */
    data class TestUser(
        val id: String = TEST_USER_ID,
        val username: String = TEST_USERNAME,
        val email: String = TEST_EMAIL,
        val isActive: Boolean = true,
        val createdAt: String = "2024-01-01T00:00:00Z",
        val updatedAt: String = "2024-01-01T00:00:00Z"
    )

    /**
     * 테스트용 프로젝트 설정 데이터 클래스
     */
    data class TestProjectConfig(
        val name: String = TEST_PROJECT_NAME,
        val version: String = TEST_VERSION,
        val isDebugMode: Boolean = true,
        val enableLogging: Boolean = true,
        val maxRetryCount: Int = 3,
        val timeoutMs: Long = 5000L
    )

    /**
     * 테스트용 애플리케이션 상태 변경 시나리오
     */
    data class TestStateTransition(
        val fromState: ApplicationState,
        val toState: ApplicationState,
        val shouldSucceed: Boolean = true,
        val errorMessage: String? = null
    )

    /**
     * 기본 사용자 Mock 데이터 생성
     */
    fun createTestUser(
        id: String = TEST_USER_ID,
        username: String = TEST_USERNAME,
        email: String = TEST_EMAIL,
        isActive: Boolean = true
    ): TestUser {
        return TestUser(
            id = id,
            username = username,
            email = email,
            isActive = isActive
        )
    }

    /**
     * 기본 프로젝트 설정 Mock 데이터 생성
     */
    fun createTestProjectConfig(
        name: String = TEST_PROJECT_NAME,
        version: String = TEST_VERSION,
        isDebugMode: Boolean = true
    ): TestProjectConfig {
        return TestProjectConfig(
            name = name,
            version = version,
            isDebugMode = isDebugMode
        )
    }

    /**
     * 여러 사용자 목록 생성
     */
    fun createTestUsers(count: Int = 3): List<TestUser> {
        return (1..count).map { index ->
            createTestUser(
                id = "user$index",
                username = "testuser$index",
                email = "test$index@example.com"
            )
        }
    }

    /**
     * 애플리케이션 상태 전환 시나리오 생성
     */
    fun createStateTransitionScenarios(): List<TestStateTransition> {
        return listOf(
            TestStateTransition(ApplicationState.CREATED, ApplicationState.INITIALIZING),
            TestStateTransition(ApplicationState.INITIALIZING, ApplicationState.READY),
            TestStateTransition(ApplicationState.READY, ApplicationState.RUNNING),
            TestStateTransition(ApplicationState.RUNNING, ApplicationState.PAUSED),
            TestStateTransition(ApplicationState.PAUSED, ApplicationState.RUNNING),
            TestStateTransition(ApplicationState.RUNNING, ApplicationState.DESTROYED),
            // 에러 시나리오
            TestStateTransition(
                ApplicationState.INITIALIZING,
                ApplicationState.ERROR,
                shouldSucceed = false,
                errorMessage = "초기화 실패"
            )
        )
    }

    /**
     * 테스트용 예외 상황 데이터 - Throwable로 수정
     */
    fun createTestExceptions(): Map<String, Throwable> {
        return mapOf(
            "network_error" to RuntimeException("네트워크 연결 실패"),
            "timeout_error" to RuntimeException("작업 시간 초과"),
            "permission_error" to RuntimeException("권한이 없습니다"), // SecurityException 대체
            "invalid_data" to IllegalArgumentException("잘못된 데이터 형식"),
            "memory_error" to RuntimeException("메모리 부족") // OutOfMemoryError 대체
        )
    }

    /**
     * 테스트용 성공 응답 시뮬레이션
     */
    fun <T> createSuccessResult(data: T): Result<T> {
        return Result.success(data)
    }

    /**
     * 테스트용 실패 응답 시뮬레이션
     */
    fun <T> createFailureResult(exception: Throwable): Result<T> {
        return Result.failure(exception)
    }

    /**
     * 테스트 실행 시간 측정을 위한 유틸리티
     */
    suspend fun simulateNetworkDelay(delayMs: Long = TEST_DELAY_MS) {
        kotlinx.coroutines.delay(delayMs)
    }
}