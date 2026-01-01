package org.example.project.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 앱 테마 Composable
 * 
 * 플랫폼별로 최적화된 폰트를 사용합니다:
 * - Android: 시스템 폰트 (한글 완벽 지원)
 * - iOS: 시스템 폰트 (한글 완벽 지원)
 * - WASM: Noto Sans KR 웹 폰트 (한글 지원 시도)
 * 
 * 사용법:
 * ```kotlin
 * AppTheme {
 *     // 앱 UI 코드
 * }
 * ```
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    // 플랫폼별 폰트 로드
    val koreanFontFamily = getKoreanFontFamily()
    
    // 플랫폼 폰트를 사용하는 Typography 생성
    val typography = Typography(
        // Display 스타일
        displayLarge = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
        ),
        displayMedium = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        ),
        
        // Headline 스타일
        headlineLarge = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        ),
        
        // Title 스타일
        titleLarge = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
        
        // Body 스타일
        bodyLarge = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        ),
        
        // Label 스타일
        labelLarge = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = koreanFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        ),
    )
    
    MaterialTheme(
        typography = typography,
        content = content
    )
}
