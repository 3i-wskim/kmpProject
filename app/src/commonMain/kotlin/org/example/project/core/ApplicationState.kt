package org.example.project.core

/**
 * 애플리케이션의 생명주기 상태를 정의하는 열거형 클래스
 *
 * KMP 환경에서 모든 플랫폼에서 공통으로 사용되는 애플리케이션 상태를 관리합니다.
 */
enum class ApplicationState {
    /** 애플리케이션이 생성된 상태 */
    CREATED,

    /** 초기화 중인 상태 */
    INITIALIZING,

    /** 사용 준비가 완료된 상태 */
    READY,

    /** 실행 중인 상태 */
    RUNNING,

    /** 일시 중지된 상태 */
    PAUSED,

    /** 에러 상태 */
    ERROR,

    /** 종료된 상태 */
    DESTROYED;

    /**
     * 현재 상태에서 다른 상태로의 전환이 가능한지 확인
     */
    fun canTransitionTo(newState: ApplicationState): Boolean {
        return when (this) {
            CREATED -> newState in listOf(INITIALIZING, DESTROYED)
            INITIALIZING -> newState in listOf(READY, ERROR, DESTROYED)
            READY -> newState in listOf(RUNNING, DESTROYED)
            RUNNING -> newState in listOf(PAUSED, DESTROYED)
            PAUSED -> newState in listOf(RUNNING, DESTROYED)
            ERROR -> newState in listOf(DESTROYED, INITIALIZING) // 에러에서 재시도 가능
            DESTROYED -> false // 종료된 상태에서는 전환 불가
        }
    }

    /**
     * 현재 상태가 활성 상태인지 확인
     */
    val isActive: Boolean
        get() = this in listOf(READY, RUNNING)

    /**
     * 현재 상태가 에러 상태인지 확인
     */
    val isError: Boolean
        get() = this == ERROR

    /**
     * 현재 상태가 종료 상태인지 확인
     */
    val isDestroyed: Boolean
        get() = this == DESTROYED
}