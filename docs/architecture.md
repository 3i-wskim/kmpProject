# Kotlin Multiplatform Clean Architecture Guidelines

## 프로젝트 개요

이 프로젝트는 클린 아키텍처를 사용하여 Android, iOS, 웹어셈블리를 지원하는 Kotlin 멀티플랫폼(KMP) 프로젝트입니다.

## 기본 원칙

- Kotlin 2.2.0과 Compose Multiplatform 사용
- Domain, Data, Presentation 레이어 간의 명확한 분리
- 의존성 역전 원칙 준수 - Domain 레이어는 외부 의존성 없음
- 모든 멀티플랫폼 코드는 Android, iOS, WASM 타겟에서 동작

## 비동기 프로그래밍

- **절대 금지**: runBlocking 사용
- **권장**: suspend 함수와 적절한 코루틴 스코프
- **리액티브**: StateFlow와 Flow 사용
- **플랫폼**: Kotlin 코루틴 사용 (플랫폼별 스레드 금지)

## 패키지 구조

```
org.example.project.{layer}.{feature}
```

## 네트워크 처리

- Android/iOS: 플랫폼별 Ktor 클라이언트
- WASM: 목 데이터 사용
- 플랫폼별 구현: expect/actual 패턴

## 의존성 주입

- 수동 DI 방식
- 각 레이어별로 DI 모듈 분리
- WASM 호환성 고려

## Domain Layer 세부 규칙

- 순수 Kotlin만 사용 (플랫폼 의존성 금지)
- 모델: data class, interface, sealed class만 사용
- Repository 인터페이스 정의 (구현체는 Data Layer에서)
- Use Case는 Repository 인터페이스만 의존
- 외부 프레임워크 import 금지 (androidx, compose, ktor-client 등)
- Flow 사용으로 리액티브 데이터 스트림
- 불변 data class로 도메인 모델
- Use Case에서 비즈니스 로직 검증
- Result<T> 타입으로 에러 처리
- 단일 책임 원칙 준수
- 비즈니스 필드 포함 (isActive, createdAt 등)

## Data Layer 세부 규칙

- 도메인 인터페이스 구현
- DTO 사용 후 도메인 모델로 매핑
- @Serializable 애노테이션 사용
- DTO ↔ Domain 매핑용 확장 함수 (toDomain(), toDto())
- 네트워크 에러 처리 및 Result 타입 반환
- expect/actual 패턴으로 DataSource 구현
- 예외 처리 후 안전한 기본값 반환

## Presentation Layer 세부 규칙

- ViewModel: androidx.lifecycle.ViewModel 상속
- StateFlow로 UI 상태 관리
- Compose: 무상태 함수
- Domain Layer Use Case만 의존
- @Composable 함수 사용
- Material 3 디자인 가이드라인
- 로딩/성공/에러 상태 처리
- viewModelScope 사용
- combine() 함수로 데이터 스트림 결합
- 에러 상태 정리 (clearError())
- WASM 호환성: kotlin.random.Random 사용

## 테스트 가이드라인

- kotlin-test 사용
- Use Case: 목 리포지토리로 독립 테스트
- ViewModel: TestDispatcher 사용
- Flow/StateFlow: kotlinx-coroutines-test
- 외부 의존성: 목 처리
- 비즈니스 로직 단위 테스트
- 에러 시나리오 및 엣지 케이스 테스트

## 빌드 구성

- Kotlin 2.2.0, Compose Multiplatform 1.9.0-beta03
- 타겟: androidTarget, iosX64, iosArm64, iosSimulatorArm64, wasmJs
- JVM toolchain 11
- 적절한 sourceSets 구성
- 플랫폼별 Ktor 엔진: OkHttp(Android), Darwin(iOS), Js(WASM)