package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("🚀 WASM 앱 시작!")
    
    try {
        ComposeViewport(document.body!!) {
            println("🎨 Compose 초기화 중...")
            
            // WASM 전용 간단한 UI (Navigation 없이)
            // 참고: Navigation Compose는 WASM에서 일부 제한사항이 있습니다.
            // Android/iOS에서는 완전한 앱(App)이 정상 작동합니다.
            Web()
            
            // 완전한 앱 (Android/iOS에서는 작동, WASM에서는 에러)
            // App()
        }
        println("✅ Compose 렌더링 성공!")
    } catch (e: Throwable) {
        println("❌ 에러 발생: ${e.message}")
        println("스택 트레이스:")
        e.printStackTrace()
    }
}