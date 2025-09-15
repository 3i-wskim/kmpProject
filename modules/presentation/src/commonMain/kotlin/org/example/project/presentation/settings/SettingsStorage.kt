package org.example.project.presentation.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 앱 설정 저장용 공용 Key-Value 스토리지
 * - 멀티플랫폼에서 동일 API로 사용하기 위해 래핑
 */
interface SettingsStorage {
    suspend fun getTheme(): Result<ThemeMode>
    suspend fun setTheme(mode: ThemeMode): Result<Unit>

    suspend fun getLanguage(): Result<LanguageCode>
    suspend fun setLanguage(code: LanguageCode): Result<Unit>
}

/**
 * 테마 모드 정의 (도메인 성격이지만 간단 설정이므로 프레젠테이션에 위치)
 */
enum class ThemeMode(val value: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String?): ThemeMode = when (value) {
            LIGHT.value -> LIGHT
            DARK.value -> DARK
            else -> SYSTEM
        }
    }
}

/**
 * 언어 코드 정의
 */
enum class LanguageCode(val value: String) {
    KO("ko"), EN("en"), JA("ja");

    companion object {
        fun fromValue(value: String?): LanguageCode = when (value) {
            EN.value -> EN
            JA.value -> JA
            else -> KO
        }
    }
}

private const val KEY_THEME = "settings.theme_mode"
private const val KEY_LANGUAGE = "settings.language_code"

/**
 * 플랫폼별 Key-Value 저장소 expect 정의
 */
expect class PlatformSettingsStore() {
    fun getStringOrNull(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}

/**
 * PlatformSettingsStore 기반 구현체
 */
class SettingsStorageImpl(
    private val store: PlatformSettingsStore
) : SettingsStorage {
    override suspend fun getTheme(): Result<ThemeMode> = withContext(Dispatchers.Default) {
        runCatching { ThemeMode.fromValue(store.getStringOrNull(KEY_THEME)) }
    }

    override suspend fun setTheme(mode: ThemeMode): Result<Unit> =
        withContext(Dispatchers.Default) {
            runCatching { store.putString(KEY_THEME, mode.value) }.map {}
        }

    override suspend fun getLanguage(): Result<LanguageCode> = withContext(Dispatchers.Default) {
        runCatching { LanguageCode.fromValue(store.getStringOrNull(KEY_LANGUAGE)) }
    }

    override suspend fun setLanguage(code: LanguageCode): Result<Unit> =
        withContext(Dispatchers.Default) {
            runCatching { store.putString(KEY_LANGUAGE, code.value) }.map {}
        }
}

/**
 * Storage 생성 팩토리
 */
fun createSettingsStorage(): SettingsStorage = SettingsStorageImpl(PlatformSettingsStore())
