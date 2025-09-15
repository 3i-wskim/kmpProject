package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.User

/**
 * 사용자 데이터 접근을 위한 Repository 인터페이스
 *
 * Domain 레이어에 위치하며, Data 레이어에서 구현됩니다.
 * 외부 의존성을 추상화하고 비즈니스 로직과 데이터 접근을 분리합니다.
 */
interface UserRepository {

    /**
     * 모든 활성 사용자 목록을 Flow로 반환합니다
     * 리액티브 프로그래밍을 위해 Flow 사용
     */
    fun getUsers(): Flow<List<User>>

    /**
     * ID로 특정 사용자를 조회합니다
     *
     * @param id 사용자 ID
     * @return 사용자 객체 또는 null
     */
    suspend fun getUserById(id: String): User?

    /**
     * 새 사용자를 추가합니다
     *
     * @param user 추가할 사용자
     * @return 성공 시 추가된 사용자, 실패 시 에러 정보
     */
    suspend fun addUser(user: User): Result<User>

    /**
     * 사용자 정보를 업데이트합니다
     *
     * @param user 업데이트할 사용자
     * @return 성공 시 업데이트된 사용자, 실패 시 에러 정보
     */
    suspend fun updateUser(user: User): Result<User>

    /**
     * 사용자를 삭제합니다
     *
     * @param id 삭제할 사용자 ID
     * @return 성공/실패 결과
     */
    suspend fun deleteUser(id: String): Result<Unit>

    /**
     * 이름으로 사용자를 검색합니다
     *
     * @param name 검색할 이름 (부분 일치)
     * @return 검색된 사용자 목록
     */
    fun searchUsersByName(name: String): Flow<List<User>>

    /**
     * 이메일로 사용자를 찾습니다
     *
     * @param email 찾을 이메일
     * @return 해당 이메일의 사용자 또는 null
     */
    suspend fun findUserByEmail(email: String): User?
}