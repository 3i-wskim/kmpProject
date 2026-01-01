package org.example.project.di

import org.example.project.core.ApplicationCore
import org.example.project.data.di.dataModule
import org.example.project.domain.di.domainModule
import org.example.project.presentation.di.presentationModule
import org.koin.dsl.module

/**
 * Core DI 모듈
 */
val coreModule = module {
    single { ApplicationCore() }
}

/**
 * 전체 앱 모듈 리스트
 * - dataModule: Data Layer (Repository 자동 등록)
 * - domainModule: Domain Layer (UseCase 자동 등록)
 * - presentationModule: Presentation Layer (ViewModel 수동, SettingsStorage 자동)
 * - coreModule: Application Core
 */
val appModules = listOf(
    dataModule,
    domainModule,
    presentationModule,
    coreModule
)
