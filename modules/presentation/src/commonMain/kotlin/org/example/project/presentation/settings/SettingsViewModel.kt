package org.example.project.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 설정 화면을 위한 ViewModel
 * - StateFlow로 테마/언어 상태를 노출
 * - suspend/Result 패턴으로 안전한 저장/조회
 */
class SettingsViewModel(
    private val storage: SettingsStorage
) : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _languageCode = MutableStateFlow(LanguageCode.KO)
    val languageCode: StateFlow<LanguageCode> = _languageCode.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // 초기 상태 로딩
        viewModelScope.launch {
            storage.getTheme()
                .onSuccess { _themeMode.value = it }
                .onFailure { _error.value = it.message }

            storage.getLanguage()
                .onSuccess { _languageCode.value = it }
                .onFailure { _error.value = it.message }
        }
    }

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            storage.setTheme(mode)
                .onSuccess { _themeMode.value = mode }
                .onFailure { _error.value = it.message }
        }
    }

    fun setLanguage(code: LanguageCode) {
        viewModelScope.launch {
            storage.setLanguage(code)
                .onSuccess { _languageCode.value = code }
                .onFailure { _error.value = it.message }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

/**
 * Composable에서 쉽게 사용할 수 있는 팩토리 헬퍼
 *
 * 참고: DI 통합을 위해서는 app 모듈의 getApplicationContainer().settingsViewModel 사용 권장
 */
fun rememberSettingsViewModel(): SettingsViewModel = SettingsViewModel(createSettingsStorage())
