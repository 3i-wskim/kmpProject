package org.example.project.prod.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.example.project.core.BaseCoroutineTest
import org.example.project.domain.model.User
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.data.repository.UserRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

/**
 * Domain Layer UseCase 통합 테스트
 *
 * 실제 구현된 Domain Layer의 UseCase들이 올바르게 동작하는지 검증합니다.
 * 비즈니스 로직과 Repository 상호작용을 테스트합니다.
 */
class DomainLayerUseCaseProdTest : BaseCoroutineTest() {

    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var addUserUseCase: AddUserUseCase

    override fun onStart() {
        // 실제 Repository 구현체 사용
        userRepository = UserRepositoryImpl()
        getUsersUseCase = GetUsersUseCase(userRepository)
        addUserUseCase = AddUserUseCase(userRepository)

        // 초기 데이터 설정
        userRepository.setUsers(
            listOf(
                User(id = "1", name = "김철수", email = "kim@test.com", isActive = true),
                User(id = "2", name = "이영희", email = "lee@test.com", isActive = false),
                User(id = "3", name = "박민수", email = "park@test.com", isActive = true)
            )
        )
    }

    override fun onStop() {
        userRepository.clearUsers()
    }

    @Test
    fun t10_GetUsersUseCase_활성_사용자만_반환_검증() = runTest {
        // Given: 활성/비활성 사용자가 혼재된 데이터
        responseBodyPrinter("초기 사용자 데이터 설정 완료")

        // When: 사용자 목록을 조회할 때
        val result = getUsersUseCase().first()
        responseBodyPrinter("조회 결과: ${result.size}명")

        // Then: 활성 사용자만 반환되어야 함
        assertEquals(2, result.size, "활성 사용자 2명만 반환되어야 함")
        assertTrue(result.all { it.isActive }, "모든 결과가 활성 상태여야 함")
        assertTrue(result.any { it.name == "김철수" }, "김철수가 포함되어야 함")
        assertTrue(result.any { it.name == "박민수" }, "박민수가 포함되어야 함")
        assertFalse(result.any { it.name == "이영희" }, "비활성 사용자는 제외되어야 함")
    }

    @Test
    fun t11_GetUsersUseCase_이름_검색_기능_검증() = runTest {
        // Given: 다양한 이름의 사용자들
        userRepository.setUsers(
            listOf(
                User(id = "1", name = "김철수", email = "kim1@test.com", isActive = true),
                User(id = "2", name = "김영희", email = "kim2@test.com", isActive = true),
                User(id = "3", name = "이민수", email = "lee@test.com", isActive = true),
                User(id = "4", name = "박김현", email = "park@test.com", isActive = true)
            )
        )

        // When: '김'으로 검색할 때
        val result = getUsersUseCase.searchByName("김").first()
        responseBodyPrinter("검색 결과: ${result.map { it.name }}")

        // Then: 김이 포함된 사용자만 반환되어야 함
        assertEquals(3, result.size, "김이 포함된 사용자 3명이 반환되어야 함")
        assertTrue(result.all { it.name.contains("김") }, "모든 결과에 '김'이 포함되어야 함")
    }

    @Test
    fun t20_AddUserUseCase_정상_사용자_추가_검증() = runTest {
        // Given: 새로운 사용자 정보
        userRepository.clearUsers()

        // When: 유효한 사용자를 추가할 때
        val newUser = User(
            id = "",
            name = "새로운사용자",
            email = "new@test.com",
            isActive = true
        )
        val result = addUserUseCase(newUser)
        responseBodyPrinter("추가 결과: $result")

        // Then: 성공적으로 추가되어야 함
        assertTrue(result.isSuccess, "사용자 추가가 성공해야 함")
        val addedUser = result.getOrNull()!!
        assertEquals("새로운사용자", addedUser.name)
        assertTrue(addedUser.id.isNotBlank(), "ID가 자동 생성되어야 함")
    }

    @Test
    fun t21_AddUserUseCase_유효성_검증_테스트() = runTest {
        // Given: 잘못된 사용자 정보
        val invalidUser = User(
            id = "",
            name = "", // 빈 이름
            email = "invalid-email", // 잘못된 이메일
            isActive = true
        )

        // When: 잘못된 사용자를 추가할 때
        val result = addUserUseCase(invalidUser)
        responseBodyPrinter("검증 결과: ${result.exceptionOrNull()?.message}")

        // Then: 유효성 검증에 실패해야 함
        assertFalse(result.isSuccess, "잘못된 데이터로 추가는 실패해야 함")
        assertTrue(
            result.exceptionOrNull()?.message?.contains("empty") == true,
            "이름이 비어있다는 에러 메시지가 있어야 함"
        )
    }

    @Test
    fun t30_중복_이메일_검증_테스트() = runTest {
        // Given: 기존 사용자가 있는 상태
        val existingUser = User(
            id = "existing",
            name = "기존사용자",
            email = "existing@test.com",
            isActive = true
        )
        userRepository.setUsers(listOf(existingUser))

        // When: 같은 이메일로 새 사용자를 추가할 때
        val duplicateUser = User(
            id = "",
            name = "중복사용자",
            email = "existing@test.com", // 중복 이메일
            isActive = true
        )
        val result = addUserUseCase(duplicateUser)
        responseBodyPrinter("중복 검증 결과: ${result.exceptionOrNull()?.message}")

        // Then: 중복 에러가 발생해야 함
        assertFalse(result.isSuccess, "중복 이메일로 추가는 실패해야 함")
        assertTrue(
            result.exceptionOrNull()?.message?.contains("already exists") == true,
            "이미 존재한다는 에러 메시지가 있어야 함"
        )
    }

    @Test
    fun t40_완성된_프로필_조회_테스트() = runTest {
        // Given: 프로필이 완성된/미완성 사용자들
        userRepository.setUsers(
            listOf(
                User(
                    id = "1",
                    name = "완성사용자",
                    email = "complete@test.com",
                    avatarUrl = "avatar.jpg",
                    isActive = true
                ),
                User(
                    id = "2",
                    name = "미완성사용자",
                    email = "incomplete@test.com",
                    avatarUrl = null,
                    isActive = true
            )
        )
        )

        // When: 완성된 프로필만 조회할 때
        val result = getUsersUseCase.getCompleteProfiles().first()
        responseBodyPrinter("완성된 프로필: ${result.map { it.name }}")

        // Then: 프로필이 완성된 사용자만 반환되어야 함
        assertEquals(1, result.size, "완성된 프로필 1개만 반환되어야 함")
        assertEquals("완성사용자", result.first().name)
        assertTrue(result.first().isProfileComplete())
    }
}