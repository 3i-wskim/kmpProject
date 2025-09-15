package org.example.project.prod.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.example.project.core.BaseCoroutineTest
import org.example.project.domain.model.User
import org.example.project.data.repository.UserRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Data Layer Repository 통합 테스트
 *
 * 실제 구현된 Data Layer의 Repository가 올바르게 동작하는지 검증합니다.
 * 데이터 저장, 조회, 수정, 삭제 기능을 테스트합니다.
 */
class DataLayerRepositoryProdTest : BaseCoroutineTest() {

    private lateinit var userRepository: UserRepositoryImpl

    override fun onStart() {
        userRepository = UserRepositoryImpl()
        println("UserRepositoryImpl 생성 완료")
    }

    override fun onStop() {
        userRepository.clearUsers()
        println("Repository 정리 완료")
    }

    @Test
    fun t10_getUsers_초기_데이터_조회_검증() = runTest {
        // Given: Repository가 초기 데이터를 가지고 있음
        responseBodyPrinter("초기 데이터 조회 테스트 시작")

        // When: 사용자 목록을 조회할 때
        val result = userRepository.getUsers().first()
        responseBodyPrinter("조회된 사용자 수: ${result.size}")

        // Then: 초기 데이터가 올바르게 반환되어야 함
        assertEquals(3, result.size, "초기 데이터 3개가 반환되어야 함")

        val kimUser = result.find { it.name == "김철수" }
        assertNotNull(kimUser, "김철수가 존재해야 함")
        assertEquals("kim@example.com", kimUser.email)
        assertTrue(kimUser.isActive)

        val leeUser = result.find { it.name == "이영희" }
        assertNotNull(leeUser, "이영희가 존재해야 함")
        assertTrue(leeUser.isActive)

        val parkUser = result.find { it.name == "박민수" }
        assertNotNull(parkUser, "박민수가 존재해야 함")
        assertFalse(parkUser.isActive, "박민수는 비활성 상태여야 함")
    }

    @Test
    fun t11_getUserById_정상_조회_검증() = runTest {
        // Given: Repository에 데이터가 있음
        val initialUsers = userRepository.getUsers().first()
        val targetUser = initialUsers.first()

        // When: ID로 특정 사용자를 조회할 때
        val result = userRepository.getUserById(targetUser.id)
        responseBodyPrinter("ID로 조회한 사용자: ${result?.name}")

        // Then: 해당 사용자가 반환되어야 함
        assertNotNull(result, "사용자가 반환되어야 함")
        assertEquals(targetUser.id, result.id)
        assertEquals(targetUser.name, result.name)
        assertEquals(targetUser.email, result.email)
    }

    @Test
    fun t12_getUserById_존재하지_않는_사용자_검증() = runTest {
        // Given: 존재하지 않는 사용자 ID
        val nonExistentId = "non-existent-id"

        // When: 존재하지 않는 ID로 조회할 때
        val result = userRepository.getUserById(nonExistentId)
        responseBodyPrinter("존재하지 않는 ID 조회 결과: $result")

        // Then: null이 반환되어야 함
        assertNull(result, "존재하지 않는 사용자는 null을 반환해야 함")
    }

    @Test
    fun t20_addUser_정상_추가_검증() = runTest {
        // Given: 새로운 사용자 정보
        val newUser = User(
            id = "new-user-1",
            name = "새로운사용자",
            email = "new@example.com",
            avatarUrl = "new-avatar.jpg",
            isActive = true
        )

        // When: 사용자를 추가할 때
        val addResult = userRepository.addUser(newUser)
        responseBodyPrinter("추가 결과: ${addResult.isSuccess}")

        // Then: 성공적으로 추가되고 조회할 수 있어야 함
        assertTrue(addResult.isSuccess, "사용자 추가가 성공해야 함")

        val retrievedUser = userRepository.getUserById("new-user-1")
        assertNotNull(retrievedUser, "추가된 사용자를 조회할 수 있어야 함")
        assertEquals("새로운사용자", retrievedUser.name)
        assertEquals("new@example.com", retrievedUser.email)
    }

    @Test
    fun t21_updateUser_정상_수정_검증() = runTest {
        // Given: 기존 사용자를 수정할 정보
        val initialUsers = userRepository.getUsers().first()
        val targetUser = initialUsers.first()
        val updatedUser = targetUser.copy(
            name = "수정된이름",
            email = "updated@example.com",
            isActive = false
        )

        // When: 사용자를 수정할 때
        val updateResult = userRepository.updateUser(updatedUser)
        responseBodyPrinter("수정 결과: ${updateResult.isSuccess}")

        // Then: 성공적으로 수정되어야 함
        assertTrue(updateResult.isSuccess, "사용자 수정이 성공해야 함")

        val retrievedUser = userRepository.getUserById(targetUser.id)
        assertNotNull(retrievedUser, "수정된 사용자를 조회할 수 있어야 함")
        assertEquals("수정된이름", retrievedUser.name)
        assertEquals("updated@example.com", retrievedUser.email)
        assertFalse(retrievedUser.isActive, "활성 상태가 수정되어야 함")
    }

    @Test
    fun t22_updateUser_존재하지_않는_사용자_수정_시도() = runTest {
        // Given: 존재하지 않는 사용자 정보
        val nonExistentUser = User(
            id = "non-existent",
            name = "존재하지않음",
            email = "nonexistent@example.com",
            isActive = true
        )

        // When: 존재하지 않는 사용자를 수정하려 할 때
        val updateResult = userRepository.updateUser(nonExistentUser)
        responseBodyPrinter("존재하지 않는 사용자 수정 결과: ${updateResult.exceptionOrNull()?.message}")

        // Then: 실패해야 함
        assertTrue(updateResult.isFailure, "존재하지 않는 사용자 수정은 실패해야 함")
        assertTrue(
            updateResult.exceptionOrNull() is NoSuchElementException,
            "NoSuchElementException이 발생해야 함"
        )
    }

    @Test
    fun t30_deleteUser_정상_삭제_검증() = runTest {
        // Given: 삭제할 사용자가 있음
        val initialUsers = userRepository.getUsers().first()
        val targetUser = initialUsers.first()
        val initialCount = initialUsers.size

        // When: 사용자를 삭제할 때
        val deleteResult = userRepository.deleteUser(targetUser.id)
        responseBodyPrinter("삭제 결과: ${deleteResult.isSuccess}")

        // Then: 성공적으로 삭제되어야 함
        assertTrue(deleteResult.isSuccess, "사용자 삭제가 성공해야 함")

        val remainingUsers = userRepository.getUsers().first()
        assertEquals(initialCount - 1, remainingUsers.size, "사용자 수가 1개 감소해야 함")

        val deletedUser = userRepository.getUserById(targetUser.id)
        assertNull(deletedUser, "삭제된 사용자는 조회되지 않아야 함")
    }

    @Test
    fun t31_deleteUser_존재하지_않는_사용자_삭제_시도() = runTest {
        // Given: 존재하지 않는 사용자 ID
        val nonExistentId = "non-existent-id"

        // When: 존재하지 않는 사용자를 삭제하려 할 때
        val deleteResult = userRepository.deleteUser(nonExistentId)
        responseBodyPrinter("존재하지 않는 사용자 삭제 결과: ${deleteResult.exceptionOrNull()?.message}")

        // Then: 실패해야 함
        assertTrue(deleteResult.isFailure, "존재하지 않는 사용자 삭제는 실패해야 함")
        assertTrue(
            deleteResult.exceptionOrNull() is NoSuchElementException,
            "NoSuchElementException이 발생해야 함"
        )
    }

    @Test
    fun t40_searchUsersByName_검색_기능_검증() = runTest {
        // Given: 다양한 이름의 사용자들을 추가
        userRepository.setUsers(
            listOf(
                User(id = "1", name = "김철수", email = "kim1@test.com", isActive = true),
                User(id = "2", name = "김영희", email = "kim2@test.com", isActive = true),
                User(id = "3", name = "이민수", email = "lee@test.com", isActive = true),
                User(id = "4", name = "박김현", email = "park@test.com", isActive = false)
            )
        )

        // When: "김"으로 검색할 때
        val searchResult = userRepository.searchUsersByName("김").first()
        responseBodyPrinter("검색 결과: ${searchResult.map { "${it.name}(${it.isActive})" }}")

        // Then: 김이 포함된 모든 사용자가 반환되어야 함 (활성/비활성 무관)
        assertEquals(3, searchResult.size, "김이 포함된 사용자 3명이 반환되어야 함")
        assertTrue(searchResult.all { it.name.contains("김") }, "모든 결과에 김이 포함되어야 함")

        // 활성/비활성 사용자 모두 포함되는지 확인
        val activeCount = searchResult.count { it.isActive }
        val inactiveCount = searchResult.count { !it.isActive }
        assertEquals(2, activeCount, "활성 사용자 2명")
        assertEquals(1, inactiveCount, "비활성 사용자 1명")
    }

    @Test
    fun t41_findUserByEmail_이메일_검색_검증() = runTest {
        // Given: 특정 이메일을 가진 사용자
        val testEmail = "kim@example.com"

        // When: 이메일로 사용자를 검색할 때
        val result = userRepository.findUserByEmail(testEmail)
        responseBodyPrinter("이메일로 찾은 사용자: ${result?.name}")

        // Then: 해당 이메일의 사용자가 반환되어야 함
        assertNotNull(result, "해당 이메일의 사용자가 반환되어야 함")
        assertEquals(testEmail, result.email)
        assertEquals("김철수", result.name)
    }

    @Test
    fun t42_findUserByEmail_대소문자_구분_없는_검색_검증() = runTest {
        // Given: 대소문자가 다른 이메일
        val upperCaseEmail = "KIM@EXAMPLE.COM"

        // When: 대문자 이메일로 검색할 때
        val result = userRepository.findUserByEmail(upperCaseEmail)
        responseBodyPrinter("대소문자 구분없는 검색 결과: ${result?.name}")

        // Then: 대소문자 구분 없이 검색되어야 함
        assertNotNull(result, "대소문자 구분 없이 검색되어야 함")
        assertEquals("kim@example.com", result.email.lowercase())
        assertEquals("김철수", result.name)
    }

    @Test
    fun t50_Repository_전체_라이프사이클_통합_테스트() = runTest {
        // Given: 깨끗한 Repository 상태
        userRepository.clearUsers()

        // When: 전체 CRUD 작업을 순차적으로 수행

        // 1. Create (추가)
        val newUser = User(
            id = "lifecycle-test",
            name = "라이프사이클테스트",
            email = "lifecycle@test.com",
            isActive = true
        )
        val addResult = userRepository.addUser(newUser)
        assertTrue(addResult.isSuccess, "사용자 추가 성공")

        // 2. Read (조회)
        val readResult = userRepository.getUserById("lifecycle-test")
        assertNotNull(readResult, "추가한 사용자 조회 성공")
        assertEquals("라이프사이클테스트", readResult.name)

        // 3. Update (수정)
        val updatedUser = readResult.copy(name = "수정된라이프사이클")
        val updateResult = userRepository.updateUser(updatedUser)
        assertTrue(updateResult.isSuccess, "사용자 수정 성공")

        val updatedReadResult = userRepository.getUserById("lifecycle-test")
        assertEquals("수정된라이프사이클", updatedReadResult?.name)

        // 4. Delete (삭제)
        val deleteResult = userRepository.deleteUser("lifecycle-test")
        assertTrue(deleteResult.isSuccess, "사용자 삭제 성공")

        val deletedReadResult = userRepository.getUserById("lifecycle-test")
        assertNull(deletedReadResult, "삭제된 사용자는 조회되지 않음")

        // Then: 전체 라이프사이클이 성공적으로 완료됨
        responseBodyPrinter("전체 CRUD 라이프사이클 테스트 완료")
    }
}