package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.data.datasource.UserRemoteDataSource
import org.example.project.data.datasource.UserRemoteDataSourceImpl
import org.example.project.data.repository.UserRepositoryImpl
import org.example.project.domain.repository.UserRepository
import org.example.project.domain.usecase.AddUserUseCase
import org.example.project.domain.usecase.GetUsersUseCase
import org.example.project.presentation.screen.UserListScreen
import org.example.project.presentation.viewmodel.UserViewModel

/**
 * Simple manual DI - works on all platforms including WASM
 */
object AppDI {
    // Data layer
    private val userRemoteDataSource: UserRemoteDataSource = UserRemoteDataSourceImpl()
    private val userRepository: UserRepository = UserRepositoryImpl(userRemoteDataSource)

    // Domain layer
    private val getUsersUseCase = GetUsersUseCase(userRepository)
    private val addUserUseCase = AddUserUseCase(userRepository)

    // Presentation layer
    val userViewModel = UserViewModel(getUsersUseCase, addUserUseCase)
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        UserListScreen(viewModel = AppDI.userViewModel)
    }
}