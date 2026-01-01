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
import org.example.project.presentation.settings.LanguageCode

/**
 * 언어 설정 스크린
 * - 앱 언어 변경
 * - 다국어 지원 설정
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
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
                    text = "언어 설정",
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
                text = "언어 선택",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LanguageOption(
                title = "한국어",
                nativeTitle = "한국어",
                code = "ko",
                flag = "🇰🇷",
                isSelected = state.languageCode == LanguageCode.KO,
                onClick = { onEventSent(SettingsContract.Event.OnLanguageChanged(LanguageCode.KO)) }
            )

            LanguageOption(
                title = "English",
                nativeTitle = "English",
                code = "en",
                flag = "🇺🇸",
                isSelected = state.languageCode == LanguageCode.EN,
                onClick = { onEventSent(SettingsContract.Event.OnLanguageChanged(LanguageCode.EN)) }
            )

            LanguageOption(
                title = "Japanese",
                nativeTitle = "日本語",
                code = "ja",
                flag = "🇯🇵",
                isSelected = state.languageCode == LanguageCode.JA,
                onClick = { onEventSent(SettingsContract.Event.OnLanguageChanged(LanguageCode.JA)) }
            )
        }
    }
}

@Composable
private fun LanguageOption(
    title: String,
    nativeTitle: String,
    code: String,
    flag: String,
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
                text = flag,
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

                if (title != nativeTitle) {
                    Text(
                        text = nativeTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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