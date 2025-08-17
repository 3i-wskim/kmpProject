package org.example.project.data.dto

import kotlinx.serialization.Serializable
import org.example.project.domain.model.User

/**
 * Data Transfer Object for User API communication.
 * This is separate from the domain model to isolate external API concerns.
 */
@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatar_url: String? = null,
    val is_active: Boolean = true,
    val created_at: String? = null,
    val updated_at: String? = null
)

/**
 * Extension function to convert DTO to Domain model
 */
fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        avatarUrl = avatar_url,
        isActive = is_active
    )
}

/**
 * Extension function to convert Domain model to DTO
 */
fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email,
        avatar_url = avatarUrl,
        is_active = isActive
    )
}