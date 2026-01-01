package org.example.project.domain.di

import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.koin.dsl.module

/**
 * Domain Layer DI 모듈
 * - UseCase들을 수동으로 등록
 */
val domainModule = module {
    // UseCase - 싱글톤으로 등록
    single { GetUsersUseCase(get()) }
    single { AddUserUseCase(get()) }
}
