package org.example.project.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.navigation.actions.SettingsNavigationActions

/**
 * 설정 메인 스크린
 * - 앱 설정 관리
 * - 각종 설정 옵션 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(
    navigationActions: SettingsNavigationActions
) {
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
                    onClick = { navigationActions.navigateBack() }
                ) {
                    Text("←")
                }
            }
        )

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
                    description = "앱 테마를 변경하세요",
                    icon = "🌙",
                    onClick = { navigationActions.navigateToTheme() }
                )
            }

            item {
                SettingItem(
                    title = "언어 설정",
                    description = "앱 언어를 변경하세요",
                    icon = "🌐",
                    onClick = { navigationActions.navigateToLanguage() }
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