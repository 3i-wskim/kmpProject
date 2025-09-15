package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.model.User
import org.example.project.domain.repository.UserRepository

/**
 * Use case for retrieving users.
 * Encapsulates business logic and depends only on domain interfaces.
 */
class GetUsersUseCase(
    private val userRepository: UserRepository
) {

    /**
     * Get all active users
     */
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getUsers()
            .map { users -> users.filter { it.isActive } }
    }

    /**
     * Get users filtered by name
     */
    fun searchByName(query: String): Flow<List<User>> {
        return userRepository.getUsers()
            .map { users ->
                users.filter { user ->
                    user.isActive && user.name.contains(query, ignoreCase = true)
                }
            }
    }

    /**
     * Get users with complete profiles
     */
    fun getCompleteProfiles(): Flow<List<User>> {
        return userRepository.getUsers()
            .map { users ->
                users.filter { user ->
                    user.isActive && user.isProfileComplete()
                }
            }
    }
}