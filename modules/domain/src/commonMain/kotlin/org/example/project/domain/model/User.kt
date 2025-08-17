package org.example.project.domain.model

/**
 * Domain model representing a User.
 * This is part of the Domain layer and has no dependencies on external frameworks.
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val isActive: Boolean = true
)