package org.example.project.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

/**
 * ApplicationCore의 초기화 및 상태 전환 테스트
 *
 * KMP에서는 kotlin-test를 사용하여 플랫폼 독립적인 테스트를 작성합니다.
 * JUnit의 기능들을 최대한 KMP에서 재현합니다.
 *
 * 테스트 함수 명명 규칙:
 * - t10, t20: 메인 테스트 그룹
 * - t11, t12: 세부 테스트
 */
class ApplicationCoreInitializationTest : BaseCoroutineTest() {

    private lateinit var applicationCore: ApplicationCore

    override fun onStart() {
        applicationCore = ApplicationCore()
        println("ApplicationCore 인스턴스 생성됨")
    }

    override fun onStop() {
        println("테스트 정리 완료")
    }

    @Test
    fun t10_애플리케이션_초기_상태_검증() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 생성됨")

        // When
        println("WHEN: 초기 상태를 확인함")
        val currentState = applicationCore.state.value
        responseBodyPrinter("초기 상태: $currentState")

        // Then
        println("THEN: 상태가 CREATED여야 함")
        assertEquals(ApplicationState.CREATED, applicationCore.state.value)
    }

    @Test
    fun t20_초기화_프로세스_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 CREATED 상태임")
        assertEquals(ApplicationState.CREATED, applicationCore.state.value)

        // When
        println("WHEN: initialize()를 호출함")
        val result = applicationCore.initialize()
        responseBodyPrinter("초기화 결과: $result")

        // Then
        println("THEN: 상태가 READY로 변경되어야 함")
        assertTrue(result.isSuccess)
        assertEquals(ApplicationState.READY, applicationCore.state.value)
    }

    @Test
    fun t21_시작_프로세스_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 READY 상태임")
        applicationCore.initialize()
        assertEquals(ApplicationState.READY, applicationCore.state.value)

        // When
        println("WHEN: start()를 호출함")
        val result = applicationCore.start()
        responseBodyPrinter("시작 결과: $result")

        // Then
        println("THEN: 상태가 RUNNING으로 변경되어야 함")
        assertTrue(result.isSuccess)
        assertEquals(ApplicationState.RUNNING, applicationCore.state.value)
    }

    @Test
    fun t22_일시정지_프로세스_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 RUNNING 상태임")
        applicationCore.initialize()
        applicationCore.start()
        assertEquals(ApplicationState.RUNNING, applicationCore.state.value)

        // When
        println("WHEN: pause()를 호출함")
        val result = applicationCore.pause()
        responseBodyPrinter("일시정지 결과: $result")

        // Then
        println("THEN: 상태가 PAUSED로 변경되어야 함")
        assertTrue(result.isSuccess)
        assertEquals(ApplicationState.PAUSED, applicationCore.state.value)
    }

    @Test
    fun t23_재개_프로세스_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 PAUSED 상태임")
        applicationCore.initialize()
        applicationCore.start()
        applicationCore.pause()
        assertEquals(ApplicationState.PAUSED, applicationCore.state.value)

        // When
        println("WHEN: resume()를 호출함")
        val result = applicationCore.resume()
        responseBodyPrinter("재개 결과: $result")

        // Then
        println("THEN: 상태가 RUNNING으로 변경되어야 함")
        assertTrue(result.isSuccess)
        assertEquals(ApplicationState.RUNNING, applicationCore.state.value)
    }

    @Test
    fun t24_종료_프로세스_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 실행 중임")
        applicationCore.initialize()
        applicationCore.start()
        assertEquals(ApplicationState.RUNNING, applicationCore.state.value)

        // When
        println("WHEN: destroy()를 호출함")
        applicationCore.destroy()

        // Then
        println("THEN: 상태가 DESTROYED로 변경되어야 함")
        assertEquals(ApplicationState.DESTROYED, applicationCore.state.value)
    }

    @Test
    fun t30_에러_시나리오_잘못된_상태에서_시작() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 CREATED 상태임")
        assertEquals(ApplicationState.CREATED, applicationCore.state.value)

        // When
        println("WHEN: 초기화 없이 바로 start()를 호출함")
        val result = applicationCore.start()
        responseBodyPrinter("에러 시나리오 결과: $result")

        // Then
        println("THEN: 실패해야 하고 상태는 변경되지 않아야 함")
        assertFalse(result.isSuccess)
        assertEquals(ApplicationState.CREATED, applicationCore.state.value)
    }

    @Test
    fun t31_에러_시나리오_잘못된_상태에서_일시정지() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 READY 상태임")
        applicationCore.initialize()
        assertEquals(ApplicationState.READY, applicationCore.state.value)

        // When
        println("WHEN: RUNNING이 아닌 상태에서 pause()를 호출함")
        val result = applicationCore.pause()
        responseBodyPrinter("에러 시나리오 결과: $result")

        // Then
        println("THEN: 실패해야 하고 상태는 변경되지 않아야 함")
        assertFalse(result.isSuccess)
        assertEquals(ApplicationState.READY, applicationCore.state.value)
    }

    @Test
    fun t32_에러_시나리오_잘못된_상태에서_재개() = runTest {
        // Given
        println("GIVEN: ApplicationCore가 RUNNING 상태임")
        applicationCore.initialize()
        applicationCore.start()
        assertEquals(ApplicationState.RUNNING, applicationCore.state.value)

        // When
        println("WHEN: PAUSED가 아닌 상태에서 resume()을 호출함")
        val result = applicationCore.resume()
        responseBodyPrinter("에러 시나리오 결과: $result")

        // Then
        println("THEN: 실패해야 함")
        assertFalse(result.isSuccess)
    }

    @Test
    fun t40_상태_변경_관찰_테스트() = runTest {
        // Given
        println("GIVEN: 상태 변경을 추적할 리스트")
        val states = mutableListOf<ApplicationState>()
        states.add(applicationCore.state.value)

        // When
        println("WHEN: 여러 상태 변경을 수행함")

        applicationCore.initialize()
        states.add(applicationCore.state.value)

        applicationCore.start()
        states.add(applicationCore.state.value)

        applicationCore.pause()
        states.add(applicationCore.state.value)

        applicationCore.resume()
        states.add(applicationCore.state.value)

        applicationCore.destroy()
        states.add(applicationCore.state.value)

        responseBodyPrinter("상태 변경 이력: $states")

        // Then
        println("THEN: 예상된 상태 순서와 일치해야 함")
        val expectedStates = listOf(
            ApplicationState.CREATED,
            ApplicationState.READY,
            ApplicationState.RUNNING,
            ApplicationState.PAUSED,
            ApplicationState.RUNNING,
            ApplicationState.DESTROYED
        )
        assertEquals(expectedStates, states)
    }

    @Test
    fun t41_애플리케이션_상태_속성_테스트() = runTest {
        // ApplicationState의 속성들을 테스트

        // Given & When & Then
        println("GIVEN/WHEN/THEN: ApplicationState 속성 검증")

        // isActive 테스트
        assertTrue(ApplicationState.READY.isActive)
        assertTrue(ApplicationState.RUNNING.isActive)
        assertFalse(ApplicationState.CREATED.isActive)
        assertFalse(ApplicationState.PAUSED.isActive)

        // isError 테스트
        assertTrue(ApplicationState.ERROR.isError)
        assertFalse(ApplicationState.RUNNING.isError)

        // isDestroyed 테스트
        assertTrue(ApplicationState.DESTROYED.isDestroyed)
        assertFalse(ApplicationState.RUNNING.isDestroyed)

        println("✓ 모든 ApplicationState 속성 테스트 완료")
    }

    @Test
    fun t42_상태_전환_가능성_테스트() = runTest {
        // ApplicationState.canTransitionTo 테스트

        println("GIVEN/WHEN/THEN: 상태 전환 가능성 검증")

        // CREATED에서 가능한 전환
        assertTrue(ApplicationState.CREATED.canTransitionTo(ApplicationState.INITIALIZING))
        assertTrue(ApplicationState.CREATED.canTransitionTo(ApplicationState.DESTROYED))
        assertFalse(ApplicationState.CREATED.canTransitionTo(ApplicationState.RUNNING))

        // READY에서 가능한 전환
        assertTrue(ApplicationState.READY.canTransitionTo(ApplicationState.RUNNING))
        assertTrue(ApplicationState.READY.canTransitionTo(ApplicationState.DESTROYED))
        assertFalse(ApplicationState.READY.canTransitionTo(ApplicationState.PAUSED))

        // DESTROYED에서는 전환 불가
        assertFalse(ApplicationState.DESTROYED.canTransitionTo(ApplicationState.CREATED))
        assertFalse(ApplicationState.DESTROYED.canTransitionTo(ApplicationState.RUNNING))

        println("✓ 모든 상태 전환 가능성 테스트 완료")
    }

    @Test
    fun t50_애플리케이션_코어_정보_조회_테스트() = runTest {
        // Given
        println("GIVEN: ApplicationCore 정보 조회 테스트")

        // When & Then
        println("WHEN/THEN: 초기 상태 정보 확인")
        assertFalse(applicationCore.isInitialized())
        assertEquals(null, applicationCore.getErrorMessage())
        responseBodyPrinter("초기 상태 정보: ${applicationCore.getStateInfo()}")

        // 초기화 후
        applicationCore.initialize()
        assertTrue(applicationCore.isInitialized())
        responseBodyPrinter("초기화 후 상태 정보: ${applicationCore.getStateInfo()}")

        println("✓ 애플리케이션 코어 정보 조회 테스트 완료")
    }

    /**
     * JUnit의 assertAll과 같은 다중 검증 테스트 예시
     */
    @Test
    fun t60_다중_검증_테스트() = runTest {
        println("GIVEN/WHEN/THEN: 다중 검증 테스트")

        // 다중 검증을 직접 수행
        println("검증 1: 초기 상태")
        assertEquals(ApplicationState.CREATED, applicationCore.state.value)

        println("검증 2: 초기화되지 않음")
        assertFalse(applicationCore.isInitialized())

        println("검증 3: 에러 메시지 없음")
        assertEquals(null, applicationCore.getErrorMessage())

        println("✓ 모든 다중 검증 완료")
    }

    /**
     * JUnit의 assertThrows와 같은 예외 테스트 예시
     */
    @Test
    fun t70_예외_상황_테스트() = runTest {
        println("GIVEN/WHEN/THEN: 예외 상황 테스트")

        // 정상적인 경우 예외가 발생하지 않아야 함 - 직접 테스트
        try {
            applicationCore.destroy() // CREATED 상태에서 destroy는 항상 가능
            println("✓ destroy() 호출 성공 - 예외 없음")
        } catch (e: Exception) {
            throw AssertionError("예외가 발생하지 않아야 하는데 발생함: ${e.message}")
        }

        println("✓ 예외 상황 테스트 완료")
    }
}