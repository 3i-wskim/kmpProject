package org.example.project.presentation.feature.settings.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.feature.settings.SettingsContract
import org.example.project.presentation.core.SIDE_EFFECTS_KEY
import org.example.project.presentation.settings.ThemeMode

/**
 * 테마 설정 스크린
 * - 앱 테마 변경
 * - 다크 모드/라이트 모드 설정
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SettingsContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsContract.Effect.Navigation -> onNavigationRequested(effect)
                is SettingsContract.Effect.ShowError -> { /* TODO: Show Snackbar */ }
            }
        }
    }

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
                    onClick = { onEventSent(SettingsContract.Event.OnBackClick) }
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
                isSelected = state.themeMode == ThemeMode.SYSTEM,
                onClick = { onEventSent(SettingsContract.Event.OnThemeChanged(ThemeMode.SYSTEM)) }
            )

            ThemeOption(
                title = "라이트 모드",
                description = "밝은 테마를 사용합니다",
                icon = "☀️",
                isSelected = state.themeMode == ThemeMode.LIGHT,
                onClick = { onEventSent(SettingsContract.Event.OnThemeChanged(ThemeMode.LIGHT)) }
            )

            ThemeOption(
                title = "다크 모드",
                description = "어두운 테마를 사용합니다",
                icon = "🌙",
                isSelected = state.themeMode == ThemeMode.DARK,
                onClick = { onEventSent(SettingsContract.Event.OnThemeChanged(ThemeMode.DARK)) }
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