package org.example.project.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * 코루틴을 사용하는 테스트를 위한 베이스 클래스 (KMP용)
 *
 * TestDispatcher를 사용하여 코루틴 테스트를 안전하고 예측 가능하게 실행합니다.
 * 테스트 실행 시간과 성능을 측정하는 기능을 제공합니다.
 * JUnit의 코루틴 테스트 기능을 KMP에서 재현합니다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseCoroutineTest {

    /**
     * 테스트용 코루틴 스케줄러
     */
    protected val testScheduler = TestCoroutineScheduler()

    /**
     * 테스트용 디스패처
     */
    protected val testDispatcher = StandardTestDispatcher(testScheduler)

    /**
     * 테스트용 스코프
     */
    protected val testScope = TestScope(testDispatcher)

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
    fun processOnStart() {
        // 메인 디스패처를 테스트 디스패처로 설정
        Dispatchers.setMain(testDispatcher)

        startTime = ++testCounter
        println("onStart (${formatCurrentTime()})")

        onStart()
    }

    @AfterTest
    fun processOnStop() {
        onStop()

        // 메인 디스패처를 원래대로 복구
        Dispatchers.resetMain()

        val duration = (++testCounter - startTime)
        println("onStop (${formatCurrentTime()})")
        println(" -> 테스트 실행 단위: ${duration}단계")
    }

    /**
     * 현재 시간을 포맷팅하여 반환
     * 테스트 실행 시간을 추적하기 위해 사용
     */
    private fun formatCurrentTime(): String {
        val currentTime = ++testCounter
        return "Step-${currentTime}"
    }

    /**
     * 코루틴 테스트에서 시간 진행을 시뮬레이션
     */
    protected fun advanceTimeBy(delayTimeMillis: Long) {
        testScheduler.advanceTimeBy(delayTimeMillis)
    }

    /**
     * 모든 대기 중인 작업을 실행
     */
    protected fun advanceUntilIdle() {
        testScheduler.advanceUntilIdle()
    }

    /**
     * Given-When-Then 패턴을 명확하게 하기 위한 헬퍼 함수들
     */
    protected fun given(description: String, block: () -> Unit) {
        println("GIVEN: $description")
        block()
    }

    protected suspend fun givenSuspend(description: String, block: suspend () -> Unit) {
        println("GIVEN: $description")
        block()
    }

    protected fun `when`(description: String, block: () -> Unit) {
        println("WHEN: $description")
        block()
    }

    protected suspend fun whenSuspend(description: String, block: suspend () -> Unit) {
        println("WHEN: $description")
        block()
    }

    protected fun then(description: String, block: () -> Unit) {
        println("THEN: $description")
        block()
    }

    protected suspend fun thenSuspend(description: String, block: suspend () -> Unit) {
        println("THEN: $description")
        block()
    }

    /**
     * runTest를 사용하여 코루틴 테스트를 실행하는 헬퍼
     */
    protected fun runCoroutineTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    /**
     * 테스트 응답 데이터를 콘솔에 출력하는 유틸리티 함수
     * 디버깅과 테스트 결과 확인에 유용합니다.
     */
    fun <T> responseBodyPrinter(response: T) {
        println("=======================================")
        println("responseBodyPrinter: $response")
        println("=======================================")
    }
}