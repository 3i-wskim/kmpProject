package org.example.project.core

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * 모든 테스트 클래스의 기본 베이스 클래스 (KMP용)
 *
 * kotlin-test를 사용하며 테스트의 공통 설정과 정리를 담당합니다.
 * Given-When-Then 패턴을 사용한 구조화된 테스트 작성을 지원합니다.
 * JUnit의 기능을 최대한 KMP에서 재현합니다.
 */
abstract class BaseTest {

    /**
     * 각 테스트 시작 시 호출되는 초기화 메서드
     * 서브클래스에서 구현하여 테스트별 초기화 로직을 작성합니다.
     */
    abstract fun onStart()

    /**
     * 각 테스트 종료 시 호출되는 정리 메서드
     * 서브클래스에서 구현하여 테스트별 정리 로직을 작성합니다.
     */
    abstract fun onStop()

    private var startTime = 0L
    private var testCounter = 0L

    @BeforeTest
    fun processOnResume() {
        startTime = ++testCounter
        println("onResume: ${formatCurrentTime()}")
        onStart()
    }

    @AfterTest
    fun processOnPause() {
        onStop()
        val duration = (++testCounter - startTime)
        println("onPause: ${formatCurrentTime()}")
        println(" -> 테스트 실행 단위: ${duration}단계")
    }

    /**
     * 현재 시간을 포맷팅하여 반환
     * 테스트 실행 시간을 추적하기 위해 사용
     */
    private fun formatCurrentTime(): String {
        val currentTime = ++testCounter
        // KMP 호환 시간 포맷팅
        return formatTimeMillis(currentTime)
    }

    /**
     * 플랫폼별 현재 시간 반환 (JUnit의 시간 기능 대체)
     */
    private fun getCurrentTimeMillis(): Long {
        return ++testCounter
    }

    /**
     * 시간을 포맷팅하는 함수 (java.text.SimpleDateFormat 대체)
     */
    private fun formatTimeMillis(timeMillis: Long): String {
        // 간단한 시간 표시 (KMP 호환)
        return "Step-${timeMillis}"
    }

    /**
     * JUnit의 assertj 스타일 기능을 제공하는 헬퍼 함수들
     */
    protected fun <T> assertThat(actual: T): AssertionHelper<T> {
        return AssertionHelper(actual)
    }

    /**
     * JUnit 스타일의 assertion 헬퍼 클래스
     */
    class AssertionHelper<T>(private val actual: T) {
        fun isEqualTo(expected: T) {
            kotlin.test.assertEquals(expected, actual)
        }

        fun isNotEqualTo(unexpected: T) {
            kotlin.test.assertNotEquals(unexpected, actual)
        }

        fun isNotNull(): AssertionHelper<T> {
            kotlin.test.assertNotNull(actual)
            return this
        }

        fun isNull() {
            kotlin.test.assertNull(actual)
        }
    }

    /**
     * Given-When-Then 패턴을 명확하게 하기 위한 헬퍼 함수들 (JUnit의 nested test 스타일)
     */
    protected fun given(description: String, block: () -> Unit) {
        println("GIVEN: $description")
        block()
    }

    protected fun `when`(description: String, block: () -> Unit) {
        println("WHEN: $description")
        block()
    }

    protected fun then(description: String, block: () -> Unit) {
        println("THEN: $description")
        block()
    }

    /**
     * 테스트 응답 데이터를 콘솔에 출력하는 유틸리티 함수
     * 디버깅과 테스트 결과 확인에 유용합니다.
     */
    protected fun <T> responseBodyPrinter(response: T) {
        println("=======================================")
        println("responseBodyPrinter: $response")
        println("=======================================")
    }

    /**
     * JUnit의 assertAll 기능을 모방한 함수
     */
    protected fun assertAll(vararg assertions: () -> Unit) {
        val failures = mutableListOf<Throwable>()

        for (assertion in assertions) {
            try {
                assertion()
            } catch (e: Throwable) {
                failures.add(e)
            }
        }

        if (failures.isNotEmpty()) {
            val message = failures.joinToString("\n") { it.message ?: "Unknown error" }
            throw AssertionError("Multiple assertion failures:\n$message")
        }
    }

    /**
     * JUnit의 assertThrows 기능을 모방한 함수
     */
    protected inline fun <reified T : Throwable> assertThrows(block: () -> Unit): T {
        try {
            block()
            throw AssertionError("Expected ${T::class.simpleName} to be thrown")
        } catch (e: Throwable) {
            if (e is T) {
                return e
            } else {
                throw AssertionError("Expected ${T::class.simpleName} but got ${e::class.simpleName}: ${e.message}")
            }
        }
    }

    /**
     * JUnit의 assertDoesNotThrow 기능을 모방한 함수
     */
    protected fun assertDoesNotThrow(block: () -> Unit) {
        try {
            block()
        } catch (e: Throwable) {
            throw AssertionError("Expected no exception to be thrown, but got ${e::class.simpleName}: ${e.message}")
        }
    }
}