package org.example.project.domain.usecase

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.example.project.domain.model.User
import org.example.project.domain.repository.UserRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUsersUseCaseTest {

    @Test
    fun `should return only active users`() = runTest {
        // Given
        val users = listOf(
            User("1", "John", "john@example.com", isActive = true),
            User("2", "Jane", "jane@example.com", isActive = false),
            User("3", "Bob", "bob@example.com", isActive = true)
        )

        val repository = object : UserRepository {
            override fun getUsers() = flow { emit(users) }
            override suspend fun getUserById(id: String): User? = null
            override suspend fun addUser(user: User) = Result.success(user)
            override suspend fun updateUser(user: User) = Result.success(user)
            override suspend fun deleteUser(id: String) = Result.success(Unit)
        }

        val useCase = GetUsersUseCase(repository)

        // When
        val result = useCase().toList().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("John", result[0].name)
        assertEquals("Bob", result[1].name)
    }

    @Test
    fun `should search users by name ignoring case`() = runTest {
        // Given
        val users = listOf(
            User("1", "John Doe", "john@example.com", isActive = true),
            User("2", "Jane Smith", "jane@example.com", isActive = true),
            User("3", "johnny walker", "johnny@example.com", isActive = true)
        )

        val repository = object : UserRepository {
            override fun getUsers() = flow { emit(users) }
            override suspend fun getUserById(id: String): User? = null
            override suspend fun addUser(user: User) = Result.success(user)
            override suspend fun updateUser(user: User) = Result.success(user)
            override suspend fun deleteUser(id: String) = Result.success(Unit)
        }

        val useCase = GetUsersUseCase(repository)

        // When
        val result = useCase.searchByName("john").toList().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("John Doe", result[0].name)
        assertEquals("johnny walker", result[1].name)
    }
}