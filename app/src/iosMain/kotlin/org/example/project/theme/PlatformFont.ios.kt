package org.example.project.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

/**
 * iOS 전용 폰트 로더
 * 
 * iOS 시스템 폰트를 사용합니다.
 * iOS는 SF Pro 및 Apple SD Gothic Neo가 기본 포함되어 한글을 완벽히 지원합니다.
 */
@Composable
actual fun getKoreanFontFamily(): FontFamily {
    // iOS 시스템 기본 폰트 (한글 완벽 지원)
    return FontFamily.SansSerif
}
