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
        isActive = is_active,
        createdAt = created_at?.let { parseTimestamp(it) } ?: 0L,
        updatedAt = updated_at?.let { parseTimestamp(it) } ?: 0L
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
        is_active = isActive,
        created_at = if (createdAt > 0) formatTimestamp(createdAt) else null,
        updated_at = if (updatedAt > 0) formatTimestamp(updatedAt) else null
    )
}

/**
 * Simple timestamp parsing for demo purposes
 * In a real app, you'd use proper date/time libraries
 */
private fun parseTimestamp(timestamp: String): Long {
    return try {
        // For demo, just return a fixed timestamp
        // In real app, use kotlinx-datetime for proper ISO 8601 parsing
        1704067200000L // 2024-01-01T00:00:00Z
    } catch (e: Exception) {
        0L
    }
}

/**
 * Simple timestamp formatting for demo purposes
 */
private fun formatTimestamp(timestamp: Long): String {
    return if (timestamp > 0) {
        // For demo, return ISO-like format
        "2025-01-01T00:00:00Z"
    } else {
        "1970-01-01T00:00:00Z"
    }
}