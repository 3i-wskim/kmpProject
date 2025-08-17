# Kotlin Multiplatform Project - Clean Architecture

This is a Kotlin Multiplatform project targeting Android, iOS, and Web (WASM) with **Clean
Architecture** implementation.

## 🏗️ Clean Architecture Structure

The project follows Clean Architecture principles with clear separation of concerns and dependency
inversion:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │────│     Domain      │    │      Data       │
│                 │    │                 │◄───│                 │
│ • UI/Screens    │    │ • Models        │    │ • Repositories  │
│ • ViewModels    │    │ • Use Cases     │    │ • Data Sources  │
│ • DI Module     │    │ • Interfaces    │    │ • DTOs/Mappers  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        ▲                       │
         └────────────────────────┼───────────────────────┘
                                  │
                        ┌─────────────────┐
                        │       App       │
                        │                 │
                        │ • Entry Points  │
                        │ • DI Setup      │
                        └─────────────────┘
```

## Project Structure (리팩토링된 구조)

### Core Modules

- **`:modules:domain`** - Business Logic Layer (No dependencies)
  - Models (User, etc.)
  - Repository Interfaces
  - Use Cases (GetUsersUseCase, AddUserUseCase)
  - Business Rules & Validation

- **`:modules:data`** - Data Access Layer (Depends on Domain)
  - Repository Implementations
  - Data Sources (Remote/Local)
  - DTOs & Mappers
  - Network/Database Access

- **`:modules:presentation`** - UI Layer (Depends on Domain)
  - Compose UI Screens
  - ViewModels
  - UI State Management
  - Platform-specific UI Logic

- **`:app`** - Application Layer (Depends on all modules)
  - Entry Points (MainActivity, main.kt)
  - Dependency Injection Setup
  - App Configuration

### Platform-specific Structure

Each module supports multiplatform with:

- `commonMain` - Shared code across all platforms
- `androidMain` - Android-specific implementations
- `iosMain` - iOS-specific implementations
- `wasmJsMain` - WebAssembly-specific implementations

## 🎯 Dependency Flow

Following the **Dependency Inversion Principle**:

```
App ──┐
      ├─→ Presentation ──┐
      │                  ├─→ Domain ←── Data
      └─→ Data ──────────┘
```

- **Domain** has no external dependencies (Pure Kotlin + Coroutines)
- **Data** depends only on Domain interfaces
- **Presentation** depends only on Domain use cases
- **App** orchestrates all modules with DI

## 🛠️ Technology Stack

- **Kotlin**: 2.2.0
- **Compose Multiplatform**: 1.9.0-beta03
- **Android Gradle Plugin**: 8.10.1
- **AndroidX Lifecycle**: 2.9.2
- **Koin**: 3.5.6 (Dependency Injection)
- **Ktor**: 2.3.12 (HTTP Client)
- **Coroutines**: 1.8.1 (Async/Reactive Programming)

## 🚀 Getting Started

### Prerequisites

- JDK 11 or later (자동 다운로드 지원)
- Android Studio or IntelliJ IDEA
- Xcode (for iOS development)

### Running on different platforms

- **Android**: Run the Android configuration in your IDE or use `./gradlew :app:installDebug`
- **iOS**: Open the project in Xcode or use `./gradlew :app:iosSimulatorArm64Test`
- **Web (WASM)**: Run `:app:wasmJsBrowserDevelopmentRun` Gradle task

### Testing

Run unit tests for specific modules:

```bash
# Domain layer tests (business logic)
./gradlew :modules:domain:testDebugUnitTest

# Data layer tests  
./gradlew :modules:data:testDebugUnitTest

# Presentation layer tests
./gradlew :modules:presentation:testDebugUnitTest

# All tests
./gradlew test
```

## Android Studio에서 웹 빌드 설정

### **1. Gradle Tasks 패널 열기**

1. **Android Studio 오른쪽**에 있는 **"Gradle"** 패널 클릭
2. 또는 **View → Tool Windows → Gradle**

### **2. 웹 빌드 Task 찾기**

Gradle 패널에서 다음 구조를 찾으세요:

```
📁 KotlinProject
└── 📁 app
    └── 📁 Tasks
        └── 📁 kotlin browser
            ├── 🚀 wasmJsBrowserDevelopmentRun    ← 개발용 실행
            ├── 📦 wasmJsBrowserDistribution       ← 프로덕션 빌드
            └── 🔧 wasmJsBrowserDevelopmentWebpack ← 개발용 빌드만
```

### **3. Run Configuration 만들기 (추천)**

더 편리하게 사용하기 위해 Run Configuration을 만들어보겠습니다:

#### **Step 1: Run Configuration 추가**
1. 상단 툴바의 **"Run"** 드롭다운 클릭
2. **"Edit Configurations..."** 선택

#### **Step 2: Gradle Task 추가**
1. 왼쪽 **"+"** 버튼 클릭
2. **"Gradle"** 선택

#### **Step 3: 웹 앱 실행 설정**
```
📝 웹 앱 실행 설정:
- Name: "🌐 Run Web App"
- Gradle project: KotlinProject
- Tasks: app:wasmJsBrowserDevelopmentRun
- Arguments: --continuous --info
```

#### **Step 4: 안드로이드 앱 실행 설정**

```
📝 안드로이드 앱 실행 설정:
- Name: "📱 Run Android App"
- Gradle project: KotlinProject
- Tasks: app:installDebug
- Arguments: (비워둠)
```

#### **Step 5: 적용 및 실행**
1. **"Apply"** → **"OK"** 클릭
2. 상단 드롭다운에서 원하는 설정 선택
3. **▶️ Run 버튼** 클릭!

## 빌드 명령어 정리

### **웹 빌드 명령어들**

```bash
# 🌐 웹 개발 서버 실행 (추천)
./gradlew :app:wasmJsBrowserDevelopmentRun

# 📦 웹 프로덕션 빌드
./gradlew :app:wasmJsBrowserDistribution

# 🔧 웹 개발 빌드만 (실행 안함)
./gradlew :app:wasmJsBrowserDevelopmentWebpack
```

### **안드로이드 빌드 명령어들**

```bash
# 📱 안드로이드 앱 설치 및 실행
./gradlew :app:installDebug

# 🔧 안드로이드 디버그 빌드만
./gradlew :app:assembleDebug

# 📦 안드로이드 릴리즈 빌드
./gradlew :app:assembleRelease
```

### **iOS 빌드 명령어들**

```bash
# 🍎 iOS 시뮬레이터 테스트
./gradlew :app:iosSimulatorArm64Test

# 🔧 iOS 프레임워크 빌드
./gradlew :app:linkDebugFrameworkIosArm64
```

## 디버깅 및 개발 설정

### **웹 디버깅 활성화**

Run Configuration의 **"Arguments"**에 추가:

```
--continuous --info --stacktrace
```

### **브라우저 개발자 도구 활용**
1. 웹 앱 실행 후 **F12** 누르기
2. **Console** 탭에서 Kotlin 로그 확인
3. **Network** 탭에서 WASM 파일 로딩 확인
4. **Application** 탭에서 리소스 확인

### **실시간 개발 (Hot Reload)**

웹 개발 시 파일 변경하면 자동으로 브라우저 새로고침됩니다!

## 성능 모니터링

### **빌드 시간 최적화**

```bash
# 병렬 빌드 활성화 (gradle.properties에 이미 설정됨)
org.gradle.parallel=true
org.gradle.caching=true

# 첫 빌드: ~2분
# 이후 빌드: ~30초
```

### **빌드 결과물 위치**
```
📁 빌드 결과물:
├── 🌐 웹: app/build/dist/wasmJs/productionExecutable/
├── 📱 안드로이드: app/build/outputs/apk/debug/
└── 🍎 iOS: app/build/bin/iosArm64/debugFramework/
```

## Key Features

- ✅ **Clean Architecture** with clear layer separation
- ✅ **Dependency Injection** using Koin
- ✅ **Reactive Programming** with Coroutines & Flow
- ✅ **Multiplatform UI** with Compose Multiplatform
- ✅ **Type Safety** with Kotlin Result & sealed classes
- ✅ **Testability** with dependency inversion
- ✅ **SOLID Principles** implementation
- ✅ **JDK Auto-download** support
- ✅ **Hot Reload** for web development

## Clean Architecture Benefits

1. **Independence**: UI, Database, and external dependencies are replaceable
2. **Testability**: Business logic can be tested without UI/DB
3. **Separation of Concerns**: Each layer has single responsibility
4. **Maintainability**: Changes in one layer don't affect others
5. **Scalability**: Easy to add new features following patterns

## Example: Adding New Feature

To add a new feature (e.g., Posts):

1. **Domain**: Create `Post` model, `PostRepository` interface, `GetPostsUseCase` in
   `modules/domain`
2. **Data**: Implement `PostRepositoryImpl`, `PostRemoteDataSource` in `modules/data`
3. **Presentation**: Create `PostViewModel`, `PostListScreen` in `modules/presentation`
4. **App**: Wire everything in DI modules in `app`

## 실행 결과

성공적으로 실행되면:

### **🌐 웹 앱** (`http://localhost:8080`)

- 사용자 목록 표시
- 검색 기능
- 새 사용자 추가
- Material3 디자인
- 실시간 Hot Reload

### **📱 안드로이드 앱**

- 네이티브 Android APK
- Play Store 배포 가능
- 완전한 네이티브 성능

### **🍎 iOS 앱**

- 네이티브 iOS .app
- App Store 배포 가능
- SwiftUI 래퍼를 통한 통합

## Learn More

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)

---
*Built with ❤️ using Kotlin Multiplatform & Clean Architecture*

**🚀 한 번에 3개 플랫폼 앱을 개발하는 마법 같은 경험을 해보세요!** ✨