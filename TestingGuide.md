# Kotlin 멀티플랫폼 프로젝트 테스팅 가이드

## 개요

본 프로젝트는 클린 아키텍처를 기반으로 한 Kotlin 멀티플랫폼(KMP) 프로젝트입니다. 각 레이어별로 체계적인 테스트 구조를 제공하며, 한국어 주석과 함께 이해하기 쉬운 테스트
코드를 작성할 수 있도록 구성되어 있습니다.

## 프로젝트 구조

```
KotlinProject/
├── application/
│   └── src/
│       ├── main/kotlin/org/example/project/application/core/
│       └── test/kotlin/org/example/project/application/core/
├── modules/
│   ├── domain/
│   │   └── src/
│   │       ├── commonMain/kotlin/org/example/project/domain/
│   │       │   ├── core/          # DomainCore 클래스
│   │       │   ├── model/         # 도메인 모델
│   │       │   ├── repository/    # Repository 인터페이스
│   │       │   └── usecase/       # Use Case 구현체
│   │       └── commonTest/kotlin/org/example/project/domain/
│   ├── data/
│   │   └── src/
│   │       ├── commonMain/kotlin/org/example/project/data/
│   │       │   ├── core/          # DataCore 클래스
│   │       │   ├── datasource/    # Data Source
│   │       │   ├── dto/           # Data Transfer Objects
│   │       │   └── repository/    # Repository 구현체
│   │       └── commonTest/kotlin/org/example/project/data/
│   └── presentation/
│       └── src/
│           ├── commonMain/kotlin/org/example/project/presentation/
│           │   ├── core/          # PresentationCore 클래스
│           │   ├── navigation/    # 네비게이션
│           │   ├── screen/        # UI 스크린
│           │   └── viewmodel/     # ViewModel
│           └── commonTest/kotlin/org/example/project/presentation/
```

## 테스트 철학

### 1. 클린 아키텍처 원칙 준수

- 각 레이어의 책임을 명확히 분리하여 테스트
- 의존성 방향을 준수하여 외부 의존성을 Mock으로 대체
- 비즈니스 로직과 UI 로직을 분리하여 테스트

### 2. 실용적인 테스트 접근

- 과도한 Mocking 지양, 실제 시나리오에 가까운 테스트 작성
- 에러 시나리오를 포함한 포괄적인 테스트 커버리지
- 한국어 주석으로 테스트 의도 명확화

### 3. Core 클래스 패턴

각 레이어마다 Core 클래스를 도입하여 다음과 같은 이점을 제공:

- 레이어별 초기화와 의존성 관리 중앙화
- 테스트하기 쉬운 구조 제공
- 상태 관리와 에러 처리 표준화

## 레이어별 테스트 가이드

### 1. Application Layer 테스트

Application Layer는 전체 애플리케이션의 진입점 역할을 하며, `ApplicationCore` 클래스를 통해 관리됩니다.

#### 테스트 대상:

- ApplicationCore 초기화 및 상태 관리
- 레이어 간 의존성 주입 검증
- 애플리케이션 생명주기 관리
- 에러 처리 및 복구 시나리오

#### 예제: ApplicationCore 테스트 패턴

```kotlin
/**
 * ApplicationCore 에러 시나리오 테스트
 *
 * ApplicationCore 클래스의 예외 상황과 에러 처리 로직을 테스트합니다.
 * Mock 객체를 최소한으로 사용하고 실제 에러 상황을 시뮬레이션합니다.
 */
@DisplayName("ApplicationCore 에러 시나리오 테스트")
class ApplicationCoreErrorTest : BaseCoroutineTest() {

    private lateinit var applicationCore: TestApplicationCore

    override fun onStart() {
        applicationCore = TestApplicationCore()
    }

    override fun onStop() {
        try {
            applicationCore.cleanup()
        } catch (e: Exception) {
            // cleanup 중 에러가 발생해도 테스트는 계속 진행
        }
    }

    @Test
    @DisplayName("t10: ApplicationCore 초기화 실패 시 예외 처리 검증")
    fun t10() = runTest {
        // Given: 초기화가 실패하도록 설정된 ApplicationCore
        applicationCore.shouldFailInitialization = true

        // When & Then: initialize() 호출 시 예외 발생
        val exception = assertFailsWith<ApplicationInitializationException> {
            applicationCore.initialize()
        }

        // Then: 적절한 예외가 발생하고 상태가 ERROR로 변경됨
        assertTrue(
            exception.message?.contains("애플리케이션 초기화 실패") == true,
            "적절한 에러 메시지가 포함되어야 함"
        )
        assertEquals(
            ApplicationState.ERROR,
            applicationCore.currentState.first(),
            "상태가 ERROR로 변경되어야 함"
        )
        assertFalse(
            applicationCore.isInitialized.first(),
            "초기화 상태는 false로 유지되어야 함"
        )
    }
}
```

### 2. Domain Layer 테스트

Domain Layer는 비즈니스 로직의 핵심으로, Use Case와 도메인 모델을 테스트합니다.

#### 테스트 대상:

- Use Case의 비즈니스 로직 검증
- 도메인 모델의 불변성과 비즈니스 규칙
- Repository 인터페이스와의 상호작용
- DomainCore의 초기화 및 의존성 관리

#### 핵심 클래스: DomainCore

```kotlin
/**
 * 도메인 레이어의 핵심 클래스
 *
 * 도메인 레이어의 초기화와 Use Case들의 생성을 관리합니다.
 * 클린 아키텍처 원칙에 따라 외부 의존성을 추상화하여 관리합니다.
 */
open class DomainCore {
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _currentState = MutableStateFlow(DomainState.NOT_INITIALIZED)
    val currentState: StateFlow<DomainState> = _currentState.asStateFlow()

    // Use Cases (지연 초기화)
    private var _getUsersUseCase: GetUsersUseCase? = null
    private var _addUserUseCase: AddUserUseCase? = null

    val getUsersUseCase: GetUsersUseCase
        get() = _getUsersUseCase ?: throw IllegalStateException("DomainCore가 초기화되지 않음")

    val addUserUseCase: AddUserUseCase
        get() = _addUserUseCase ?: throw IllegalStateException("DomainCore가 초기화되지 않음")
}
```

#### Use Case 테스트 예제:

```kotlin
/**
 * GetUsersUseCase 테스트
 * 사용자 목록 조회와 관련된 비즈니스 로직을 검증합니다.
 * - 활성 사용자만 반환하는 로직
 * - 이름으로 사용자 검색 로직
 */
class GetUsersUseCaseTest {

    @Test
    fun `활성 사용자만 반환해야 함`() = runTest {
        // Given
        val mockRepository = MockUserRepository()
        val testUsers = listOf(
            User("1", "활성사용자1", "active1@test.com", isActive = true),
            User("2", "비활성사용자", "inactive@test.com", isActive = false),
            User("3", "활성사용자2", "active2@test.com", isActive = true)
        )
        mockRepository.users = testUsers
        val useCase = GetUsersUseCase(mockRepository)

        // When
        val result = useCase().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.isActive })
        assertEquals("활성사용자1", result[0].name)
        assertEquals("활성사용자2", result[1].name)
    }
}
```

### 3. Data Layer 테스트

Data Layer는 외부 데이터 소스와의 연동을 담당하며, Repository 구현체와 Data Source를 테스트합니다.

#### 테스트 대상:

- Repository 구현체의 데이터 변환 로직
- Data Source의 네트워크/로컬 DB 연동
- 에러 처리 및 예외 상황 대응
- DataCore의 초기화 및 컴포넌트 관리

#### 핵심 클래스: DataCore

```kotlin
/**
 * 데이터 레이어의 핵심 클래스
 *
 * 데이터 레이어의 초기화와 Repository 구성을 관리합니다.
 * 데이터 소스들과 Repository 구현체들의 생성과 의존성 주입을 담당합니다.
 */
open class DataCore {
    // Data Sources (지연 초기화)
    private var _userRemoteDataSource: UserRemoteDataSource? = null
    
    // Repositories (지연 초기화)
    private var _userRepository: UserRepository? = null

    val userRepository: UserRepository
        get() = _userRepository ?: throw IllegalStateException("DataCore가 초기화되지 않음")

    suspend fun initialize() {
        try {
            _currentState.value = DataState.INITIALIZING
            performInitialization()
            _currentState.value = DataState.READY
            _isInitialized.value = true
        } catch (e: Exception) {
            _currentState.value = DataState.ERROR
            _isInitialized.value = false
            throw DataInitializationException("데이터 레이어 초기화 실패", e)
        }
    }
}
```

#### Repository 테스트 예제:

```kotlin
/**
 * UserRepositoryImpl 테스트
 *
 * UserRepository 구현체의 데이터 소스 연동과 에러 처리를 테스트합니다.
 * Repository 패턴의 데이터 변환과 예외 처리 로직을 검증합니다.
 */
class UserRepositoryImplTest {

    @Test
    fun `정상적인 사용자 목록 조회 시 도메인 모델로 변환되어야 함`() = runTest {
        // Given: 정상적인 UserDto 목록을 반환하는 DataSource
        val userDtos = listOf(
            UserDto(id = "1", name = "김철수", email = "kim@test.com"),
            UserDto(id = "2", name = "이영희", email = "lee@test.com")
        )
        val mockDataSource = TestUserRemoteDataSource(userDtos)
        val repository = UserRepositoryImpl(mockDataSource)

        // When: 사용자 목록 조회
        val result = repository.getUsers().first()

        // Then: 도메인 모델로 올바르게 변환됨
        assertEquals(2, result.size)
        assertEquals("김철수", result[0].name)
        assertEquals("kim@test.com", result[0].email)
    }

    @Test
    fun `데이터 소스에서 예외 발생 시 빈 리스트를 반환해야 함`() = runTest {
        // Given: 예외를 발생시키는 DataSource
        val mockDataSource = TestUserRemoteDataSource(shouldThrowException = true)
        val repository = UserRepositoryImpl(mockDataSource)

        // When: 사용자 목록 조회
        val result = repository.getUsers().first()

        // Then: 빈 리스트 반환
        assertTrue(result.isEmpty())
    }
}
```

### 4. Presentation Layer 테스트

Presentation Layer는 UI 상태 관리와 사용자 상호작용을 담당하며, ViewModel과 UI 컴포넌트를 테스트합니다.

#### 테스트 대상:

- ViewModel의 상태 관리 로직
- UI 이벤트 처리 및 사용자 상호작용
- Use Case와의 연동 검증
- PresentationCore의 초기화 및 ViewModel 관리

#### 핵심 클래스: PresentationCore

```kotlin
/**
 * 프레젠테이션 레이어의 핵심 클래스
 *
 * 프레젠테이션 레이어의 초기화와 ViewModel 관리를 담당합니다.
 * UI 관련 컴포넌트들의 생성과 의존성 주입을 관리합니다.
 */
open class PresentationCore {
    // ViewModels (지연 초기화)
    private var _userViewModel: UserViewModel? = null

    val userViewModel: UserViewModel
        get() = _userViewModel ?: throw IllegalStateException("PresentationCore가 초기화되지 않음")

    suspend fun initialize(
        getUsersUseCase: GetUsersUseCase,
        addUserUseCase: AddUserUseCase
    ) {
        try {
            _currentState.value = PresentationState.INITIALIZING
            performInitialization(getUsersUseCase, addUserUseCase)
            _currentState.value = PresentationState.READY
            _isInitialized.value = true
        } catch (e: Exception) {
            _currentState.value = PresentationState.ERROR
            throw PresentationInitializationException("프레젠테이션 레이어 초기화 실패", e)
        }
    }
}
```

## 통합 테스트 예제

### 전체 애플리케이션 플로우 테스트

```kotlin
/**
 * 전체 애플리케이션 플로우 통합 테스트
 * 모든 레이어가 연동되어 동작하는 시나리오를 검증합니다.
 */
class ApplicationIntegrationTest {

    @Test
    fun `사용자 추가 및 조회 전체 플로우 테스트`() = runTest {
        // Given: 전체 애플리케이션 초기화
        val dataCore = DataCore()
        dataCore.initialize()

        val domainCore = DomainCore()
        domainCore.initialize(dataCore.userRepository)

        val presentationCore = PresentationCore()
        presentationCore.initialize(
            domainCore.getUsersUseCase,
            domainCore.addUserUseCase
        )

        val viewModel = presentationCore.userViewModel

        // When: 사용자 추가
        viewModel.addUser("김철수", "kim@test.com")

        // Then: 사용자 목록에 추가된 사용자가 포함됨
        val users = viewModel.uiState.first().users
        assertTrue(users.any { it.name == "김철수" && it.email == "kim@test.com" })

        // Cleanup
        presentationCore.cleanup()
        domainCore.cleanup()
        dataCore.cleanup()
    }
}
```

## 테스트 실행 방법

### Gradle을 통한 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 모듈 테스트 실행
./gradlew :modules:domain:test
./gradlew :modules:data:test
./gradlew :modules:presentation:test
./gradlew :application:test

# 특정 테스트 클래스 실행
./gradlew test --tests "*ApplicationCoreTest"
./gradlew test --tests "*DomainCoreTest"

# 테스트 보고서 생성
./gradlew test --continue
# 보고서 위치: build/reports/tests/test/index.html
```

### IDE에서 테스트 실행

1. **IntelliJ IDEA / Android Studio**:
    - 테스트 클래스나 메서드 옆의 초록색 실행 버튼 클릭
    - 프로젝트 창에서 테스트 폴더 우클릭 → "Run Tests"

2. **테스트 스위트 실행**:
    - `ApplicationCoreTestSuite` 클래스 실행으로 관련 모든 테스트 일괄 실행

## 테스트 작성 가이드라인

### 1. 테스트 네이밍 컨벤션

- 한국어 메서드명 사용: `정상적인_초기화_시_모든_UseCase가_사용_가능해야_함()`
- 또는 backtick 사용: `` `정상적인 초기화 시 모든 UseCase가 사용 가능해야 함`() ``
- DisplayName 어노테이션 활용: `@DisplayName("t10: 초기화 실패 시 예외 처리 검증")`

### 2. 테스트 구조 (AAA Pattern)

```kotlin
@Test
fun `테스트_시나리오_설명`() = runTest {
    // Given: 테스트를 위한 준비 상태 설정
    val mockRepository = TestUserRepository()
    val useCase = GetUsersUseCase(mockRepository)

    // When: 실제 테스트하려는 동작 수행
    val result = useCase().first()

    // Then: 결과 검증 및 어설션
    assertTrue(result.isEmpty())
    assertEquals(expectedState, actualState)
}
```

### 3. 에러 시나리오 테스트

```kotlin
@Test
fun `초기화_실패_시_적절한_예외_발생`() = runTest {
    // Given: 실패 조건 설정
    val coreBroken = TestCore(shouldFail = true)

    // When & Then: 예외 발생 검증
    val exception = assertFailsWith<InitializationException> {
        coreBroken.initialize()
    }

    assertTrue(exception.message?.contains("초기화 실패") == true)
    assertEquals(ErrorState.ERROR, coreBroken.currentState.first())
}
```

### 4. 상태 관리 테스트

```kotlin
@Test
fun `상태_변화_시나리오_검증`() = runTest {
    // Given: 초기 상태 확인
    assertEquals(State.NOT_INITIALIZED, core.currentState.first())

    // When: 상태 변경 동작
    core.initialize()

    // Then: 상태 변화 확인
    assertEquals(State.READY, core.currentState.first())
    assertTrue(core.isInitialized.first())
}
```

## 테스트 베스트 프랙티스

### 1. Mock 사용 최소화

- 실제 동작에 가까운 테스트 더블 활용
- 비즈니스 로직에 집중한 테스트 작성
- 외부 의존성만 Mock으로 처리

### 2. 테스트 독립성 보장

- 각 테스트는 독립적으로 실행 가능해야 함
- setUp/tearDown 메서드 활용으로 테스트 간 상태 격리
- 테스트 순서에 의존하지 않는 설계

### 3. 의미 있는 어설션

- 단순한 값 비교를 넘어 비즈니스 의미 검증
- 에러 메시지에 한국어 설명 포함
- 테스트 실패 시 원인을 쉽게 파악할 수 있도록 구성

### 4. 성능 고려사항

- 대용량 데이터 시나리오 테스트 포함
- 코루틴 기반 비동기 처리 검증
- 메모리 누수 및 리소스 정리 확인

## 결론

본 테스팅 가이드를 통해 클린 아키텍처 기반의 Kotlin 멀티플랫폼 프로젝트에서 체계적이고 효율적인 테스트를 작성할 수 있습니다. 각 레이어의 Core 클래스를 활용하여
테스트하기 쉬운 구조를 만들고, 실제 비즈니스 시나리오에 맞는 포괄적인 테스트를 통해 안정적인 애플리케이션을 개발할 수 있습니다.

테스트는 단순한 코드 검증을 넘어 비즈니스 요구사항의 구현을 확인하고, 리팩토링 시 안전성을 보장하는 중요한 도구입니다. 지속적인 테스트 작성과 개선을 통해 더 나은 소프트웨어
품질을 달성하시기 바랍니다.