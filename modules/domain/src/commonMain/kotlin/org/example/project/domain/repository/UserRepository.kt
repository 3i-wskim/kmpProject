package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.User

/**
 * Repository interface for User operations.
 * This is part of the Domain layer and defines the contract for data access.
 * The actual implementation will be in the Data layer.
 */
interface UserRepository {

    /**
     * Get all users as a Flow for reactive updates
     */
    fun getUsers(): Flow<List<User>>

    /**
     * Get a specific user by ID
     */
    suspend fun getUserById(id: String): User?

    /**
     * Add a new user
     */
    suspend fun addUser(user: User): Result<User>

    /**
     * Update an existing user
     */
    suspend fun updateUser(user: User): Result<User>

    /**
     * Delete a user by ID
     */
    suspend fun deleteUser(id: String): Result<Unit>
}