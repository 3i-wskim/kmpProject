package org.example.project.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

/**
 * Android 전용 폰트 로더
 * 
 * Android 시스템 폰트를 사용합니다.
 * Android는 Roboto 및 Noto Sans CJK가 기본 포함되어 한글을 완벽히 지원합니다.
 */
@Composable
actual fun getKoreanFontFamily(): FontFamily {
    // Android 시스템 기본 폰트 (한글 완벽 지원)
    return FontFamily.SansSerif
}
