package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.di.appModules
import org.example.project.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.example.project.presentation.navigation.AppNavigation

@Composable
@Preview
fun App() {
    // Koin DI 초기화
    KoinApplication(
        application = {
            try {
                modules(appModules)
                println("✅ Koin 초기화 성공: ${getPlatform().name}")
            } catch (e: Exception) {
                println("❌ Koin 초기화 실패: ${e.message}")
                e.printStackTrace()
            }
        }
    ) {
        // 커스텀 Typography로 모든 텍스트의 폰트를 일괄 적용
        AppTheme {
            println("📱 AppNavigation 시작...")
            AppNavigation()
        }
    }
}