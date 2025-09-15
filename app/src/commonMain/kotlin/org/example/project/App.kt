package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.project.presentation.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 메인 앱 컴포저블
 * - 네비게이션 시스템을 사용하여 다양한 화면들을 관리
 * - Material 3 디자인 시스템 적용
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        // 네비게이션 시스템을 통해 앱 화면들을 관리
        AppNavigation()
    }
}