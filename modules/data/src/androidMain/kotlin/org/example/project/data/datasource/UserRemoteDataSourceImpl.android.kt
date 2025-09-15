package org.example.project.data.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.data.dto.UserDto

/**
 * Android-specific implementation using Ktor HTTP client
 */
actual class UserRemoteDataSourceImpl : UserRemoteDataSource {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "https://api.example.com"

    actual override suspend fun getUsers(): List<UserDto> {
        return try {
            httpClient.get("$baseUrl/users").body()
        } catch (e: Exception) {
            // Fallback to mock data for demo purposes
            getMockUsers()
        }
    }

    actual override suspend fun getUserById(id: String): UserDto? {
        return try {
            httpClient.get("$baseUrl/users/$id").body()
        } catch (e: Exception) {
            getMockUsers().find { it.id == id }
        }
    }

    actual override suspend fun createUser(user: UserDto): UserDto {
        return try {
            httpClient.post("$baseUrl/users") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body()
        } catch (e: Exception) {
            user.copy(created_at = "2025-01-01T00:00:00Z")
        }
    }

    actual override suspend fun updateUser(user: UserDto): UserDto {
        return try {
            httpClient.put("$baseUrl/users/${user.id}") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body()
        } catch (e: Exception) {
            user.copy(updated_at = "2025-01-01T00:00:00Z")
        }
    }

    actual override suspend fun deleteUser(id: String) {
        try {
            httpClient.delete("$baseUrl/users/$id")
        } catch (e: Exception) {
            // Handle error silently for demo
        }
    }

    private fun getMockUsers(): List<UserDto> {
        return listOf(
            UserDto(
                id = "1",
                name = "John Doe (Android)",
                email = "john@example.com",
                avatar_url = "https://example.com/avatar1.png",
                is_active = true
            ),
            UserDto(
                id = "2",
                name = "Jane Smith (Android)",
                email = "jane@example.com",
                avatar_url = "https://example.com/avatar2.png",
                is_active = true
            ),
            UserDto(
                id = "3",
                name = "Inactive User (Android)",
                email = "inactive@example.com",
                is_active = false
            )
        )
    }
}