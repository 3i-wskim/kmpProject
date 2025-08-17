package org.example.project.data.datasource

import kotlinx.coroutines.delay
import org.example.project.data.dto.UserDto

/**
 * WASM-specific implementation using mock data (no network dependency)
 */
actual class UserRemoteDataSourceImpl : UserRemoteDataSource {

    private val mockUsers = listOf(
        UserDto(
            id = "1",
            name = "Alice Johnson",
            email = "alice@example.com",
            avatar_url = "https://example.com/avatar1.png",
            is_active = true
        ),
        UserDto(
            id = "2",
            name = "Bob Smith",
            email = "bob@example.com",
            avatar_url = "https://example.com/avatar2.png",
            is_active = true
        ),
        UserDto(
            id = "3",
            name = "Charlie Brown",
            email = "charlie@example.com",
            is_active = false
        ),
        UserDto(
            id = "4",
            name = "Diana Prince",
            email = "diana@example.com",
            avatar_url = "https://example.com/avatar4.png",
            is_active = true
        )
    )

    private val userList = mockUsers.toMutableList()

    actual override suspend fun getUsers(): List<UserDto> {
        // Simulate network delay
        delay(500)
        return userList.toList()
    }

    actual override suspend fun getUserById(id: String): UserDto? {
        // Simulate network delay
        delay(200)
        return userList.find { it.id == id }
    }

    actual override suspend fun createUser(user: UserDto): UserDto {
        // Simulate network delay
        delay(300)

        val newId = ((userList.maxByOrNull { it.id.toIntOrNull() ?: 0 }?.id?.toIntOrNull()
            ?: 0) + 1).toString()
        val newUser = user.copy(
            id = newId,
            created_at = "2025-01-01T00:00:00Z"
        )
        userList.add(newUser)
        return newUser
    }

    actual override suspend fun updateUser(user: UserDto): UserDto {
        // Simulate network delay
        delay(250)

        val index = userList.indexOfFirst { it.id == user.id }
        if (index >= 0) {
            val updatedUser = user.copy(updated_at = "2025-01-01T00:00:00Z")
            userList[index] = updatedUser
            return updatedUser
        }
        return user
    }

    actual override suspend fun deleteUser(id: String) {
        // Simulate network delay
        delay(200)
        userList.removeAll { it.id == id }
    }
}