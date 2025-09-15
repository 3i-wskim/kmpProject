package org.example.project.di

import org.example.project.core.ApplicationCore
import org.example.project.data.datasource.UserRemoteDataSourceImpl
import org.example.project.data.repository.UserRepositoryImpl
import org.example.project.domain.repository.UserRepository
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.viewmodel.UserViewModel
import org.example.project.presentation.settings.SettingsStorage
import org.example.project.presentation.settings.SettingsStorageImpl
import org.example.project.presentation.settings.PlatformSettingsStore
import org.example.project.presentation.settings.SettingsViewModel

/**
 * 애플리케이션 범위 스코프 정의
 *
 * 수동 DI를 사용하여 각 레이어별로 의존성을 관리합니다.
 */
annotation class ApplicationScope

/**
 * 수동 DI용 AppComponent 구현체
 *
 * - 각 의존성을 한 곳에서 생성/연결합니다
 * - 멀티플랫폼/wasm 호환 (리플렉션/코드 생성 의존 없음)
 * - 클린 아키텍처의 의존성 역전 원칙을 준수합니다
 */
class AppComponent {

    // 데이터 소스 (싱글톤 생명주기)
    private val userRemoteDataSource by lazy { UserRemoteDataSourceImpl() }

    // 데이터 레이어 구현체 (싱글톤 생명주기)
    private val userRepositoryImpl: UserRepositoryImpl by lazy {
        UserRepositoryImpl(userRemoteDataSource)
    }

    // 설정 저장소 (싱글톤 생명주기)
    private val platformSettingsStore by lazy { PlatformSettingsStore() }
    private val settingsStorageImpl: SettingsStorageImpl by lazy {
        SettingsStorageImpl(platformSettingsStore)
    }

    // 도메인에서 사용하는 인터페이스로 노출
    val userRepository: UserRepository by lazy { userRepositoryImpl }
    val settingsStorage: SettingsStorage by lazy { settingsStorageImpl }

    // 유스케이스 (도메인 레이어)
    val getUsersUseCase: GetUsersUseCase by lazy { GetUsersUseCase(userRepository) }
    val addUserUseCase: AddUserUseCase by lazy { AddUserUseCase(userRepository) }

    // ViewModel (프레젠테이션 레이어)
    val userViewModel: UserViewModel by lazy { UserViewModel(getUsersUseCase, addUserUseCase) }
    val settingsViewModel: SettingsViewModel by lazy { SettingsViewModel(settingsStorage) }

    // 코어 의존성
    val applicationCore: ApplicationCore by lazy { ApplicationCore() }
}

/**
 * 애플리케이션 컨테이너 - thread-safe 싱글톤
 *
 * - 앱 전역에서 DI 컴포넌트를 노출합니다
 * - 의존성 그래프를 한 곳에서 관리합니다
 */
object ApplicationContainer {

    // AppComponent 인스턴스를 lazy로 생성 (thread-safe)
    private val component: AppComponent by lazy { AppComponent() }

    // 의존성들을 컴포넌트에서 위임
    val userRepository: UserRepository by lazy { component.userRepository }
    val settingsStorage: SettingsStorage by lazy { component.settingsStorage }
    val getUsersUseCase: GetUsersUseCase by lazy { component.getUsersUseCase }
    val addUserUseCase: AddUserUseCase by lazy { component.addUserUseCase }
    val userViewModel: UserViewModel by lazy { component.userViewModel }
    val settingsViewModel: SettingsViewModel by lazy { component.settingsViewModel }
    val applicationCore: ApplicationCore by lazy { component.applicationCore }
}

/**
 * 애플리케이션 컨테이너 접근 함수
 */
fun getApplicationContainer(): ApplicationContainer = ApplicationContainer