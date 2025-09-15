package org.example.project.prod.presentation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.example.project.core.BaseCoroutineTest
import org.example.project.domain.model.User
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.data.repository.UserRepositoryImpl
import org.example.project.presentation.viewmodel.UserViewModel
import org.example.project.presentation.viewmodel.UserUiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Presentation Layer ViewModel 통합 테스트
 *
 * 실제 구현된 Presentation Layer의 ViewModel이 올바르게 동작하는지 검증합니다.
 * UI 상태 관리, Use Case와의 연동, 에러 처리, 사용자 상호작용을 테스트합니다.
 */
class PresentationLayerViewModelProdTest : BaseCoroutineTest() {

    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var userViewModel: UserViewModel

    override fun onStart() {
        // 실제 구현체들로 전체 스택 구성
        userRepository = UserRepositoryImpl()
        getUsersUseCase = GetUsersUseCase(userRepository)
        addUserUseCase = AddUserUseCase(userRepository)
        userViewModel = UserViewModel(getUsersUseCase, addUserUseCase)

        // 초기 테스트 데이터 설정
        userRepository.setUsers(
            listOf(
                User(id = "1", name = "김철수", email = "kim@test.com", isActive = true),
                User(id = "2", name = "이영희", email = "lee@test.com", isActive = true),
                User(id = "3", name = "박민수", email = "park@test.com", isActive = false)
            )
        )

        println("ViewModel과 전체 레이어 스택 초기화 완료")
    }

    override fun onStop() {
        userRepository.clearUsers()
        println("ViewModel 테스트 정리 완료")
    }

    @Test
    fun t10_초기_상태_검증() = runTest {
        // Given: ViewModel이 초기화됨 (onStart에서)
        responseBodyPrinter("초기 상태 검증 테스트 시작")

        // When: 초기 UI 상태를 확인할 때
        advanceUntilIdle() // 초기 데이터 로딩 대기
        val initialUiState = userViewModel.uiState.first()
        val initialSearchQuery = userViewModel.searchQuery.first()

        responseBodyPrinter("초기 UI 상태: loading=${initialUiState.isLoading}, users=${initialUiState.users.size}, error=${initialUiState.error}")

        // Then: 초기 상태가 올바르게 설정되어야 함
        assertFalse(initialUiState.isLoading, "초기 로딩이 완료되어야 함")
        assertEquals(2, initialUiState.users.size, "활성 사용자 2명이 표시되어야 함")
        assertNull(initialUiState.error, "초기 에러는 null이어야 함")
        assertEquals("", initialSearchQuery, "초기 검색어는 빈 문자열이어야 함")

        // 활성 사용자만 표시되는지 확인
        assertTrue(initialUiState.users.all { it.isActive }, "모든 표시된 사용자가 활성 상태여야 함")
    }

    @Test
    fun t20_검색어_변경_시_UI_상태_업데이트() = runTest {
        // Given: ViewModel이 초기화됨
        advanceUntilIdle() // 초기 상태 대기

        // When: 검색어를 변경할 때
        userViewModel.onSearchQueryChanged("김")
        advanceUntilIdle() // 검색 결과 대기

        val searchQuery = userViewModel.searchQuery.first()
        val uiState = userViewModel.uiState.first()

        responseBodyPrinter("검색 결과: query='$searchQuery', users=${uiState.users.map { it.name }}")

        // Then: 검색어와 결과가 업데이트되어야 함
        assertEquals("김", searchQuery, "검색어가 올바르게 설정되어야 함")
        assertEquals(1, uiState.users.size, "김철수 1명만 표시되어야 함")
        assertEquals("김철수", uiState.users.first().name, "김철수가 검색되어야 함")
        assertFalse(uiState.isLoading, "검색 완료 후 로딩이 끝나야 함")
        assertNull(uiState.error, "검색 중 에러가 없어야 함")
    }

    @Test
    fun t21_빈_검색어로_변경_시_전체_목록_표시() = runTest {
        // Given: 검색 상태인 ViewModel
        userViewModel.onSearchQueryChanged("김")
        advanceUntilIdle()

        // When: 검색어를 빈 문자열로 변경할 때
        userViewModel.onSearchQueryChanged("")
        advanceUntilIdle()

        val searchQuery = userViewModel.searchQuery.first()
        val uiState = userViewModel.uiState.first()

        responseBodyPrinter("전체 목록 복원: query='$searchQuery', users=${uiState.users.size}명")

        // Then: 전체 활성 사용자 목록이 표시되어야 함
        assertEquals("", searchQuery, "검색어가 빈 문자열로 초기화되어야 함")
        assertEquals(2, uiState.users.size, "전체 활성 사용자 2명이 표시되어야 함")
        assertTrue(uiState.users.any { it.name == "김철수" }, "김철수가 포함되어야 함")
        assertTrue(uiState.users.any { it.name == "이영희" }, "이영희가 포함되어야 함")
    }

    @Test
    fun t30_사용자_추가_기능_검증() = runTest {
        // Given: ViewModel이 초기 상태에 있음
        advanceUntilIdle()
        val initialCount = userViewModel.uiState.first().users.size

        // When: 새 사용자를 추가할 때
        userViewModel.addUser("새로운사용자", "new@test.com")
        advanceUntilIdle() // 추가 작업 대기

        val finalUiState = userViewModel.uiState.first()
        responseBodyPrinter("사용자 추가 후 상태: users=${finalUiState.users.size}명, error=${finalUiState.error}")

        // Then: 사용자가 성공적으로 추가되어야 함
        assertEquals(initialCount + 1, finalUiState.users.size, "사용자가 1명 추가되어야 함")
        assertTrue(
            finalUiState.users.any { it.name == "새로운사용자" },
            "새로운사용자가 목록에 나타나야 함"
        )
        assertNull(finalUiState.error, "추가 중 에러가 없어야 함")
        assertFalse(finalUiState.isLoading, "추가 완료 후 로딩이 끝나야 함")
    }

    @Test
    fun t31_잘못된_데이터로_사용자_추가_시도() = runTest {
        // Given: ViewModel이 초기 상태에 있음
        advanceUntilIdle()

        // When: 잘못된 데이터로 사용자를 추가할 때
        userViewModel.addUser("", "invalid-email") // 빈 이름, 잘못된 이메일
        advanceUntilIdle()

        val uiState = userViewModel.uiState.first()
        responseBodyPrinter("잘못된 데이터 추가 시도 결과: error=${uiState.error}")

        // Then: 에러가 발생해야 함
        assertNotNull(uiState.error, "유효성 검증 에러가 발생해야 함")
        assertTrue(
            uiState.error.contains("empty") || uiState.error.contains("format"),
            "이름 또는 이메일 관련 에러 메시지가 있어야 함"
        )
        assertFalse(uiState.isLoading, "에러 발생 후 로딩이 끝나야 함")
    }

    @Test
    fun t40_에러_클리어_기능_검증() = runTest {
        // Given: 에러 상태인 ViewModel
        userViewModel.addUser("", "invalid") // 에러 발생시킴
        advanceUntilIdle()

        val errorState = userViewModel.uiState.first()
        assertNotNull(errorState.error, "에러가 발생한 상태여야 함")

        // When: 에러를 클리어할 때
        userViewModel.clearError()

        val clearedState = userViewModel.uiState.first()
        responseBodyPrinter("에러 클리어 후 상태: error=${clearedState.error}")

        // Then: 에러가 클리어되어야 함
        assertNull(clearedState.error, "에러가 클리어되어야 함")
    }

    @Test
    fun t50_완성된_프로필_조회_기능_검증() = runTest {
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
        userViewModel.loadCompleteProfiles()
        advanceUntilIdle()

        val uiState = userViewModel.uiState.first()
        responseBodyPrinter("완성된 프로필 조회 결과: ${uiState.users.map { "${it.name}(avatar:${it.avatarUrl != null})" }}")

        // Then: 완성된 프로필만 표시되어야 함
        assertEquals(1, uiState.users.size, "완성된 프로필 1개만 표시되어야 함")
        assertEquals("완성사용자", uiState.users.first().name)
        assertNotNull(uiState.users.first().avatarUrl, "완성된 프로필은 아바타 URL이 있어야 함")
        assertNull(uiState.error, "완성된 프로필 조회 중 에러가 없어야 함")
    }

    @Test
    fun t60_UI_상태_속성_검증() = runTest {
        // Given: 다양한 상태의 UiState
        advanceUntilIdle()

        // When: 현재 UI 상태의 속성들을 확인할 때
        val uiState = userViewModel.uiState.first()

        responseBodyPrinter("UI 상태 속성: isEmpty=${uiState.isEmpty}, hasError=${uiState.hasError}, isSuccess=${uiState.isSuccess}")

        // Then: UI 상태 속성들이 올바르게 계산되어야 함
        assertFalse(uiState.isEmpty, "사용자가 있으므로 비어있지 않아야 함")
        assertFalse(uiState.hasError, "에러가 없으므로 hasError가 false여야 함")
        assertTrue(uiState.isSuccess, "로딩 완료, 에러 없음, 데이터 있음이므로 성공 상태여야 함")
    }

    @Test
    fun t70_ViewModel_생명주기_통합_테스트() = runTest {
        // Given: 초기 상태
        advanceUntilIdle()

        // When: 여러 작업을 연속으로 수행할 때
        // 1. 검색
        userViewModel.onSearchQueryChanged("이")
        advanceUntilIdle()
        val searchState = userViewModel.uiState.first()

        // 2. 사용자 추가
        userViewModel.addUser("이새로운", "new-lee@test.com")
        advanceUntilIdle()
        val addedState = userViewModel.uiState.first()

        // 3. 검색어 초기화
        userViewModel.onSearchQueryChanged("")
        advanceUntilIdle()
        val finalState = userViewModel.uiState.first()

        responseBodyPrinter("생명주기 테스트 결과: 검색(${searchState.users.size}) -> 추가(${addedState.users.size}) -> 전체(${finalState.users.size})")

        // Then: 모든 작업이 순차적으로 올바르게 수행되어야 함
        assertEquals(1, searchState.users.size, "이영희만 검색되어야 함")
        assertEquals("이영희", searchState.users.first().name)

        assertEquals(2, addedState.users.size, "검색 상태에서 새 사용자가 추가되어 2명")
        assertTrue(addedState.users.any { it.name == "이새로운" }, "새 사용자가 추가되어야 함")

        assertEquals(4, finalState.users.size, "최종적으로 모든 활성 사용자가 표시되어야 함")
        assertNull(finalState.error, "최종 에러 상태는 null이어야 함")
    }

    @Test
    fun t80_검색과_필터링_조합_테스트() = runTest {
        // Given: 다양한 데이터 설정
        userRepository.setUsers(
            listOf(
                User(id = "1", name = "김철수", email = "kim@test.com", isActive = true),
                User(id = "2", name = "김영희", email = "kim-young@test.com", isActive = true),
                User(id = "3", name = "이김수", email = "lee-kim@test.com", isActive = true),
                User(id = "4", name = "박김현", email = "park-kim@test.com", isActive = false) // 비활성
            )
        )
        advanceUntilIdle()

        // When: "김"으로 검색할 때
        userViewModel.onSearchQueryChanged("김")
        advanceUntilIdle()

        val searchResult = userViewModel.uiState.first()
        responseBodyPrinter("검색+필터링 결과: ${searchResult.users.map { "${it.name}(active:${it.isActive})" }}")

        // Then: 활성 사용자 중 김이 포함된 사용자만 표시되어야 함
        assertEquals(3, searchResult.users.size, "활성 사용자 중 김이 포함된 3명만 표시되어야 함")
        assertTrue(searchResult.users.all { it.isActive }, "모든 결과가 활성 사용자여야 함")
        assertTrue(searchResult.users.all { it.name.contains("김") }, "모든 결과에 김이 포함되어야 함")
        assertFalse(searchResult.users.any { it.name == "박김현" }, "비활성 사용자는 제외되어야 함")
    }
}