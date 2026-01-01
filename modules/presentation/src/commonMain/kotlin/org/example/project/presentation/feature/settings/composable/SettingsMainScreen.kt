package org.example.project.presentation.feature.settings.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.feature.settings.SettingsContract
import org.example.project.presentation.core.SIDE_EFFECTS_KEY
import org.example.project.presentation.settings.LanguageCode
import org.example.project.presentation.settings.ThemeMode

/**
 * 설정 메인 스크린
 * - 앱 설정 관리
 * - 각종 설정 옵션 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SettingsContract.Effect.Navigation) -> Unit,
) {
    // Effect 처리
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsContract.Effect.Navigation -> onNavigationRequested(effect)
                is SettingsContract.Effect.ShowError -> {
                    // TODO: Show Snackbar
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 상단 앱 바
        TopAppBar(
            title = {
                Text(
                    text = "설정",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onEventSent(SettingsContract.Event.OnBackClick) }
                ) {
                    Text("←")
                }
            }
        )
        
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        // 설정 목록
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingItem(
                    title = "테마 설정",
                    description = when(state.themeMode) {
                        ThemeMode.SYSTEM -> "시스템 설정"
                        ThemeMode.LIGHT -> "라이트 모드"
                        ThemeMode.DARK -> "다크 모드"
                    },
                    icon = "🌙",
                    onClick = { onEventSent(SettingsContract.Event.OnThemeClick) }
                )
            }

            item {
                SettingItem(
                    title = "언어 설정",
                    description = when(state.languageCode) {
                        LanguageCode.KO -> "한국어"
                        LanguageCode.EN -> "English"
                        LanguageCode.JA -> "日本語"
                    },
                    icon = "🌐",
                    onClick = { onEventSent(SettingsContract.Event.OnLanguageClick) }
                )
            }

            item {
                SettingItem(
                    title = "알림 설정",
                    description = "푸시 알림을 관리하세요",
                    icon = "🔔",
                    onClick = { /* TODO: 알림 설정 페이지 */ }
                )
            }

            item {
                SettingItem(
                    title = "개인정보 설정",
                    description = "데이터 및 개인정보 관리",
                    icon = "🔐",
                    onClick = { /* TODO: 개인정보 설정 페이지 */ }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                SettingItem(
                    title = "앱 정보",
                    description = "버전 및 개발 정보",
                    icon = "ℹ️",
                    onClick = { /* TODO: 앱 정보 페이지 */ }
                )
            }

            item {
                SettingItem(
                    title = "캐시 클리어",
                    description = "저장된 데이터를 삭제합니다",
                    icon = "🗑️",
                    onClick = { /* TODO: 캐시 클리어 기능 */ }
                )
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "→",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}