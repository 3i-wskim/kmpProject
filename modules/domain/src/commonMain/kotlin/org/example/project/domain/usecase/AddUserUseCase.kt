package org.example.project.domain.usecase

import org.example.project.domain.model.User
import org.example.project.domain.repository.UserRepository

/**
 * Use case for adding a new user.
 * Contains business validation logic.
 */
class AddUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(user: User): Result<User> {
        // Business validation
        if (user.name.isBlank()) {
            return Result.failure(IllegalArgumentException("User name cannot be empty"))
        }

        if (!isValidEmail(user.email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }

        // Check if user with same email already exists
        val existingUser = userRepository.getUserById(user.id)
        if (existingUser != null) {
            return Result.failure(IllegalStateException("User with this ID already exists"))
        }

        return userRepository.addUser(user)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return emailRegex.matches(email)
    }
}