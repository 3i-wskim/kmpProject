package org.example.project.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.navigation.actions.SettingsNavigationActions
import org.example.project.presentation.settings.ThemeMode
import org.example.project.presentation.settings.rememberSettingsViewModel

/**
 * 테마 설정 스크린
 * - 앱 테마 변경
 * - 다크 모드/라이트 모드 설정
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    navigationActions: SettingsNavigationActions
) {
    val viewModel = remember { rememberSettingsViewModel() }
    val selectedTheme by viewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "테마 설정",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "테마 선택",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ThemeOption(
                title = "시스템 기본",
                description = "시스템 설정을 따릅니다",
                icon = "📱",
                isSelected = selectedTheme == ThemeMode.SYSTEM,
                onClick = { viewModel.setTheme(ThemeMode.SYSTEM) }
            )

            ThemeOption(
                title = "라이트 모드",
                description = "밝은 테마를 사용합니다",
                icon = "☀️",
                isSelected = selectedTheme == ThemeMode.LIGHT,
                onClick = { viewModel.setTheme(ThemeMode.LIGHT) }
            )

            ThemeOption(
                title = "다크 모드",
                description = "어두운 테마를 사용합니다",
                icon = "🌙",
                isSelected = selectedTheme == ThemeMode.DARK,
                onClick = { viewModel.setTheme(ThemeMode.DARK) }
            )
        }
    }
}

@Composable
private fun ThemeOption(
    title: String,
    description: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
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

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}