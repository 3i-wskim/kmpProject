# Kotlin Multiplatform Clean Architecture - Test Structure

## 🏗️ 프로젝트 테스트 구조

이 프로젝트는 `org.example.project` 패키지를 사용하는 Kotlin 멀티플랫폼(KMP) 프로젝트로, 클린 아키텍처 원칙에 따라 Domain, Data,
Presentation 레이어별로 테스트가 구성되어 있습니다.

### 📁 전체 테스트 디렉토리 구조

```
modules/
├── domain/
│   └── src/
│       └── commonTest/
│           └── kotlin/
│               └── org/
│                   └── example/
│                       └── project/
│                           └── domain/
│                               ├── model/
│                               │   └── UserTest.kt                 # 도메인 모델 테스트
│                               └── usecase/
│                                   ├── GetUsersUseCaseTest.kt      # 사용자 조회 비즈니스 로직 테스트
│                                   └── AddUserUseCaseTest.kt       # 사용자 추가 비즈니스 로직 테스트
├── data/
│   └── src/
│       └── commonTest/
│           └── kotlin/
│               └── org/
│                   └── example/
│                       └── project/
│                           └── data/
│                               └── dto/
│                                   └── UserDtoTest.kt               # DTO ↔ Domain 매핑 테스트
└── presentation/
    └── src/
        └── commonTest/
            └── kotlin/
                └── org/
                    └── example/
                        └── project/
                            └── presentation/
                                └── viewmodel/
                                    └── UserViewModelTest.kt          # ViewModel UI 상태 테스트
```

## 🧪 레이어별 테스트 상세

### 1. Domain Layer Tests (`modules/domain/src/commonTest/`)

#### UserTest.kt

- **목적**: 도메인 모델의 불변성과 비즈니스 규칙 검증
- **테스트 시나리오**:
    - 유효한 사용자 객체 생성
    - 기본값 설정 검증 (`avatarUrl = null`, `isActive = true`)
    - data class의 `equals()`, `hashCode()`, `copy()` 동작 검증

#### GetUsersUseCaseTest.kt

- **목적**: 사용자 목록 조회 비즈니스 로직 검증
- **테스트 시나리오**:
    - 활성 사용자만 필터링하여 반환
    - 이름 검색 기능 (대소문자 구분 없음)
    - 빈 목록 처리
    - Flow 기반 리액티브 데이터 스트림 테스트

#### AddUserUseCaseTest.kt

- **목적**: 사용자 추가 비즈니스 검증 로직 테스트
- **테스트 시나리오**:
    - 이름 유효성 검증 (빈 값, 공백 체크)
    - 이메일 형식 검증 (정규표현식)
    - 중복 ID 확인
    - Repository 계층 에러 전파

### 2. Data Layer Tests (`modules/data/src/commonTest/`)

#### UserDtoTest.kt

- **목적**: DTO와 Domain 모델 간 매핑 로직 검증
- **테스트 시나리오**:
    - `UserDto.toDomain()` 매핑 정확성
    - `User.toDto()` 매핑 정확성
    - null 값 처리 (`avatar_url`, `created_at`, `updated_at`)
    - 라운드트립 매핑 일관성 (DTO → Domain → DTO)
    - 기본값 설정 검증

### 3. Presentation Layer Tests (`modules/presentation/src/commonTest/`)

#### UserViewModelTest.kt

- **목적**: UI 상태 관리와 사용자 상호작용 테스트
- **테스트 시나리오**:
    - 초기 상태 검증
    - 활성 사용자 필터링 UI 반영
    - 검색어 기반 실시간 필터링
    - 사용자 추가 기능
    - 에러 상태 관리 (`clearError()`)
    - 로딩 상태 관리
    - StateFlow/Flow 기반 리액티브 UI 업데이트

## 🔧 테스트 실행 방법

### 1. 전체 테스트 실행

```bash
# 모든 모듈의 테스트 실행
./gradlew test

# 특정 모듈만 테스트
./gradlew :modules:domain:test
./gradlew :modules:data:test
./gradlew :modules:presentation:test
```

### 2. IDE에서 실행

- IntelliJ IDEA / Android Studio에서 각 테스트 클래스 또는 메서드를 개별 실행
- 패키지 단위로 실행하여 레이어별 테스트 수행

## 📚 테스트 기술 스택

### 의존성

- `kotlin-test`: 멀티플랫폼 테스트 프레임워크
- `kotlinx-coroutines-test`: 코루틴 테스트 지원
- `runTest`, `TestDispatcher`: 비동기 테스트

### 테스트 패턴

- **Given-When-Then**: 명확한 테스트 구조
- **목(Mock) 객체**: 의존성 격리
- **한국어 메서드명**: 비즈니스 요구사항 명확화

## 🎯 테스트 원칙

### 1. 클린 아키텍처 준수

```kotlin
// Domain Layer - 외부 의존성 없이 순수 비즈니스 로직 테스트
class GetUsersUseCaseTest {
    private class MockUserRepository : UserRepository // 목 구현체
}

// Data Layer - 매핑 로직과 외부 API 통신 테스트
class UserDtoTest {
    // DTO ↔ Domain 변환 테스트
}

// Presentation Layer - UI 상태와 사용자 상호작용 테스트
class UserViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
}
```

### 2. 의존성 역전 (Dependency Inversion)

- 각 레이어는 하위 레이어의 인터페이스만 의존
- 테스트에서는 목 구현체를 통해 의존성 격리

### 3. 단일 책임 원칙 (Single Responsibility)

- 각 테스트는 하나의 기능만 검증
- 명확한 테스트 메서드명으로 의도 표현

## 🚀 확장 가능한 테스트 구조

### 향후 추가 고려사항

1. **Repository 구현체 테스트** (Data Layer)
   ```
   modules/data/src/commonTest/.../repository/
   ├── UserRepositoryImplTest.kt        # Repository 구현체 테스트
   ```

2. **DataSource 테스트** (Data Layer)
   ```
   modules/data/src/commonTest/.../datasource/
   ├── UserRemoteDataSourceTest.kt      # 네트워크 API 테스트
   ```

3. **통합 테스트** (별도 모듈)
   ```
   modules/test-integration/
   └── src/commonTest/.../integration/
       └── UserFlowIntegrationTest.kt   # End-to-End 테스트
   ```

4. **UI 테스트** (Presentation Layer)
   ```
   modules/presentation/src/commonTest/.../ui/
   └── UserScreenTest.kt                # Compose UI 테스트
   ```

## 📊 테스트 커버리지 목표

| 레이어 | 목표 커버리지 | 중점 영역 |
|--------|---------------|-----------|
| Domain | 100% | 비즈니스 로직, Use Case |
| Data | 90%+ | DTO 매핑, 네트워크 에러 처리 |
| Presentation | 85%+ | UI 상태 관리, 사용자 상호작용 |

## 🔄 지속적 통합 (CI)

### GitHub Actions 예시

```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Run Tests
        run: ./gradlew test
      - name: Upload Test Results
        uses: actions/upload-artifact@v2
        with:
          name: test-results
          path: |
            modules/*/build/reports/tests/
```

이 테스트 구조는 Kotlin 멀티플랫폼 프로젝트에서 클린 아키텍처 원칙을 유지하면서 각 레이어의 책임을 명확히 분리하여 테스트할 수 있도록 설계되었습니다.