package org.example.project.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

/**
 * WASM 전용 폰트 로더
 * 
 * WASM에서는 Compose Multiplatform의 커스텀 폰트 지원이 제한적입니다.
 * 시스템 기본 폰트를 사용하며, 한글 표시가 제한될 수 있습니다.
 * 
 * 참고: Android/iOS에서는 한글이 완벽히 지원됩니다.
 */
@Composable
actual fun getKoreanFontFamily(): FontFamily {
    // WASM: 브라우저 기본 폰트 사용
    // 한글 지원은 브라우저 환경에 따라 다를 수 있습니다
    return FontFamily.SansSerif
}
