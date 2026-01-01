package org.example.project.data.di

import org.example.project.data.datasource.UserRemoteDataSource
import org.example.project.data.datasource.UserRemoteDataSourceImpl
import org.example.project.data.repository.UserRepositoryImpl
import org.example.project.domain.repository.UserRepository
import org.koin.dsl.module

/**
 * Data Layer DI 모듈
 * - Repository와 DataSource를 수동으로 등록
 */
val dataModule = module {
    // Platform-specific DataSource (expect/actual)
    // 인터페이스로 바인딩해야 Repository에서 주입받을 수 있음
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl() }
    
    // Repository - 인터페이스로 바인딩
    single<UserRepository> { UserRepositoryImpl(get()) }
}
