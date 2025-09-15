package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.example.project.data.datasource.UserRemoteDataSource
import org.example.project.data.dto.toDomain
import org.example.project.data.dto.toDto
import org.example.project.domain.model.User
import org.example.project.domain.repository.UserRepository

/**
 * Implementation of UserRepository interface.
 * This class is in the Data layer and depends on the Domain layer interface.
 * It coordinates between different data sources (remote, local, cache).
 */
class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun getUsers(): Flow<List<User>> = flow {
        try {
            val userDtos = remoteDataSource.getUsers()
            val users = userDtos.map { it.toDomain() }
            emit(users)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getUserById(id: String): User? {
        return try {
            remoteDataSource.getUserById(id)?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addUser(user: User): Result<User> {
        return try {
            val userDto = user.toDto()
            val createdUserDto = remoteDataSource.createUser(userDto)
            Result.success(createdUserDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val userDto = user.toDto()
            val updatedUserDto = remoteDataSource.updateUser(userDto)
            Result.success(updatedUserDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteUser(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchUsersByName(name: String): Flow<List<User>> = flow {
        try {
            val userDtos = remoteDataSource.getUsers()
            val users = userDtos
                .map { it.toDomain() }
                .filter { user ->
                    user.name.contains(name, ignoreCase = true)
                }
            emit(users)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun findUserByEmail(email: String): User? {
        return try {
            val userDtos = remoteDataSource.getUsers()
            val user = userDtos
                .map { it.toDomain() }
                .find { it.email.equals(email, ignoreCase = true) }
            user
        } catch (e: Exception) {
            null
        }
    }
}