package org.example.project.core

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay

/**
 * 애플리케이션의 핵심 상태 관리를 담당하는 클래스
 *
 * KMP 환경에서 모든 플랫폼에서 공통으로 사용되는 애플리케이션 라이프사이클을 관리합니다.
 * StateFlow를 사용하여 리액티브 상태 관리를 제공합니다.
 */
class ApplicationCore {

    private val _state = MutableStateFlow(ApplicationState.CREATED)

    /** 현재 애플리케이션 상태 (읽기 전용) */
    val state: StateFlow<ApplicationState> = _state.asStateFlow()

    private var isInitialized = false
    private var errorMessage: String? = null

    /**
     * 애플리케이션을 초기화합니다
     *
     * @return 초기화 성공 시 Result.success, 실패 시 Result.failure
     */
    suspend fun initialize(): Result<Unit> {
        return try {
            if (!_state.value.canTransitionTo(ApplicationState.INITIALIZING)) {
                return Result.failure(
                    IllegalStateException("현재 상태(${_state.value})에서 초기화할 수 없습니다.")
                )
            }

            _state.value = ApplicationState.INITIALIZING

            // 초기화 시뮬레이션 (실제로는 DI 설정, 리소스 로딩 등이 여기에 위치)
            delay(100) // 초기화 시간 시뮬레이션

            // 초기화 완료
            isInitialized = true
            errorMessage = null
            _state.value = ApplicationState.READY

            Result.success(Unit)
        } catch (e: Exception) {
            _state.value = ApplicationState.ERROR
            errorMessage = e.message
            Result.failure(e)
        }
    }

    /**
     * 애플리케이션을 시작합니다
     *
     * @return 시작 성공 시 Result.success, 실패 시 Result.failure
     */
    fun start(): Result<Unit> {
        return try {
            if (!_state.value.canTransitionTo(ApplicationState.RUNNING)) {
                return Result.failure(
                    IllegalStateException("현재 상태(${_state.value})에서 시작할 수 없습니다.")
                )
            }

            if (!isInitialized) {
                return Result.failure(
                    IllegalStateException("애플리케이션이 초기화되지 않았습니다.")
                )
            }

            _state.value = ApplicationState.RUNNING
            errorMessage = null
            Result.success(Unit)

        } catch (e: Exception) {
            _state.value = ApplicationState.ERROR
            errorMessage = e.message
            Result.failure(e)
        }
    }

    /**
     * 애플리케이션을 일시 중지합니다
     *
     * @return 일시 중지 성공 시 Result.success, 실패 시 Result.failure
     */
    fun pause(): Result<Unit> {
        return try {
            if (!_state.value.canTransitionTo(ApplicationState.PAUSED)) {
                return Result.failure(
                    IllegalStateException("현재 상태(${_state.value})에서 일시 중지할 수 없습니다.")
                )
            }

            _state.value = ApplicationState.PAUSED
            Result.success(Unit)

        } catch (e: Exception) {
            _state.value = ApplicationState.ERROR
            errorMessage = e.message
            Result.failure(e)
        }
    }

    /**
     * 애플리케이션을 재개합니다
     *
     * @return 재개 성공 시 Result.success, 실패 시 Result.failure
     */
    fun resume(): Result<Unit> {
        return try {
            if (_state.value != ApplicationState.PAUSED) {
                return Result.failure(
                    IllegalStateException("일시 중지 상태가 아닙니다: ${_state.value}")
                )
            }

            return start() // 재개는 start와 동일한 로직
        } catch (e: Exception) {
            _state.value = ApplicationState.ERROR
            errorMessage = e.message
            Result.failure(e)
        }
    }

    /**
     * 애플리케이션을 종료합니다
     */
    fun destroy() {
        try {
            // 리소스 정리 등의 종료 작업 수행
            isInitialized = false
            errorMessage = null
            _state.value = ApplicationState.DESTROYED
        } catch (e: Exception) {
            // 종료 과정에서 에러가 발생해도 최종적으로는 DESTROYED 상태로 변경
            errorMessage = e.message
            _state.value = ApplicationState.DESTROYED
        }
    }

    /**
     * 현재 에러 메시지를 반환합니다
     */
    fun getErrorMessage(): String? = errorMessage

    /**
     * 애플리케이션이 초기화되었는지 확인합니다
     */
    fun isInitialized(): Boolean = isInitialized

    /**
     * 현재 상태를 문자열로 반환합니다 (디버깅용)
     */
    fun getStateInfo(): String {
        return "State: ${_state.value}, Initialized: $isInitialized, Error: $errorMessage"
    }
}