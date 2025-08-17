package org.example.project.data.datasource

import org.example.project.data.dto.UserDto

/**
 * Interface for remote data source operations
 */
interface UserRemoteDataSource {
    suspend fun getUsers(): List<UserDto>
    suspend fun getUserById(id: String): UserDto?
    suspend fun createUser(user: UserDto): UserDto
    suspend fun updateUser(user: UserDto): UserDto
    suspend fun deleteUser(id: String)
}