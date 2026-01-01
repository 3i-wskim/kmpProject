package org.example.project.presentation.di

import org.example.project.presentation.feature.home.HomeViewModel
import org.example.project.presentation.feature.onboarding.OnboardingViewModel
import org.example.project.presentation.feature.profile.ProfileViewModel
import org.example.project.presentation.feature.settings.SettingsViewModel
import org.example.project.presentation.feature.splash.SplashViewModel
import org.example.project.presentation.feature.user.UserViewModel
import org.example.project.presentation.feature.userdetail.UserDetailViewModel
import org.example.project.presentation.feature.userlist.UserListViewModel
import org.example.project.presentation.settings.PlatformSettingsStore
import org.example.project.presentation.settings.SettingsStorage
import org.example.project.presentation.settings.SettingsStorageImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Presentation Layer DI 모듈
 * - SettingsStorage와 ViewModel을 수동으로 등록
 */
val presentationModule = module {
    // Platform-specific Settings Store (expect/actual)
    single { PlatformSettingsStore() }
    
    // Settings Storage - 인터페이스로 바인딩
    single<SettingsStorage> { SettingsStorageImpl(get()) }
    
    // ViewModels - 수동 정의 (파라미터 없음)
    viewModel { SplashViewModel() }
    viewModel { OnboardingViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { UserListViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { UserViewModel(get(), get()) }
    
    // ViewModels - 파라미터 있음 (parametersOf 지원)
    viewModel { (userId: Long) -> 
        UserDetailViewModel(userId, get()) 
    }
    viewModel { (userId: Long, isEditMode: Boolean) -> 
        ProfileViewModel(userId, isEditMode, get()) 
    }
}
