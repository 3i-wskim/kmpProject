package org.example.project.data.datasource

import org.example.project.data.dto.UserDto

/**
 * Platform-specific implementation of UserRemoteDataSource
 * - Android/iOS: Uses Ktor HTTP client
 * - WASM: Uses mock data (no network dependencies)
 */
expect class UserRemoteDataSourceImpl() : UserRemoteDataSource {
    override suspend fun getUsers(): List<UserDto>
    override suspend fun getUserById(id: String): UserDto?
    override suspend fun createUser(user: UserDto): UserDto
    override suspend fun updateUser(user: UserDto): UserDto
    override suspend fun deleteUser(id: String)
}