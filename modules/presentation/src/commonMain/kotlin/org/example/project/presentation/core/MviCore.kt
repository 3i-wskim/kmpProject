package org.example.project.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val SIDE_EFFECTS_KEY = "side-effects_key"

/**
 * UI 상태를 나타내는 마커 인터페이스
 * 화면에 표시될 모든 데이터는 이 인터페이스를 구현하는 클래스(주로 Data Class)에 포함되어야 합니다.
 */
interface BaseUiState

/**
 * 사용자의 행동(Intent)이나 시스템 이벤트를 나타내는 마커 인터페이스
 */
interface BaseUiEvent

/**
 * 단발성 이벤트(Side Effect)를 나타내는 마커 인터페이스
 * 예: Toast 메시지 표시, 화면 이동, 스낵바 등
 */
interface BaseUiEffect

/**
 * MVI 패턴을 위한 Base ViewModel
 *
 * @param S UiState 타입
 * @param E UiEvent 타입
 * @param F UiEffect 타입
 */
abstract class BaseViewModel<S : BaseUiState, E : BaseUiEvent, F : BaseUiEffect>(
    initialState: S
) : ViewModel() {

    // UI 상태 관리
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // 단발성 이벤트(Effect) 관리
    private val _effect = Channel<F>()
    val effect: Flow<F> = _effect.receiveAsFlow()

    /**
     * UI 이벤트를 처리하는 추상 함수
     */
    abstract fun onEvent(event: E)

    /**
     * UI 상태 업데이트를 위한 도우미 함수
     */
    protected fun setState(reduce: S.() -> S) {
        _uiState.update(reduce)
    }

    /**
     * 단발성 이벤트를 발생시키는 도우미 함수
     */
    protected fun setEffect(builder: () -> F) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }
}