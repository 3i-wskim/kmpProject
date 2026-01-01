package org.example.project.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * 플랫폼별 폰트 로더
 * 
 * - Android/iOS: 시스템 폰트 사용
 * - WASM: 웹 폰트 또는 임베디드 폰트 사용
 */
@Composable
expect fun getKoreanFontFamily(): FontFamily

/**
 * 폴백 폰트 패밀리
 * expect 함수 사용 불가 시 사용
 */
val DefaultKoreanFontFamily = FontFamily.SansSerif
