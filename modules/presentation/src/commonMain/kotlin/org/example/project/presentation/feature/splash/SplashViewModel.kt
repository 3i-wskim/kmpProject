package org.example.project.presentation.feature.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.presentation.core.BaseViewModel
class SplashViewModel : BaseViewModel<SplashContract.State, SplashContract.Event, SplashContract.Effect>(
    initialState = SplashContract.State
) {

    init {
        // 초기화 로직이 있다면 여기서 실행
    }

    override fun onEvent(event: SplashContract.Event) {
        when (event) {
            is SplashContract.Event.OnAnimationEnd -> {
                viewModelScope.launch {
                    // 예시: 3초 후 이동 (실제 로직은 UseCase 호출 등)
                    delay(3000)
                    setEffect { SplashContract.Effect.Navigation.GoToHome }
                }
            }
        }
    }
}