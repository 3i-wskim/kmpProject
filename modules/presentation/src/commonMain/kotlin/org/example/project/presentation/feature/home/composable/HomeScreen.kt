package org.example.project.presentation.feature.home.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.core.SIDE_EFFECTS_KEY
import org.example.project.presentation.feature.home.HomeContract

/**
 * 홈 스크린
 * - 앱의 메인 화면
 * - 주요 기능들로의 네비게이션 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: HomeContract.Effect.Navigation) -> Unit,
) {
    // Effect 처리 (Navigation, Toast 등)
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is HomeContract.Effect.Navigation -> onNavigationRequested(effect)
                is HomeContract.Effect.ShowToast -> {
                    // TODO: Show Toast
                    println("Toast: ${effect.message}")
                }
                is HomeContract.Effect.ShowError -> {
                    // TODO: Show Snackbar or Dialog
                    println("Error: ${effect.message}")
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
                    text = "KMP 홈",
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { onEventSent(HomeContract.Event.OnClickSettings) }
                ) {
                    Text("⚙️")
                }
            }
        )

        // 메인 콘텐츠
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WelcomeCard()
            }

            item {
                NavigationCard(
                    title = "사용자 목록",
                    description = "등록된 사용자들을 확인하세요",
                    emoji = "👥",
                    onClick = { onEventSent(HomeContract.Event.OnClickUserList) }
                )
            }

            item {
                NavigationCard(
                    title = "프로필",
                    description = "내 프로필을 관리하세요",
                    emoji = "👤",
                    onClick = {
                        // 임시 사용자 ID (1L)로 프로필 이동 이벤트 발생
                        onEventSent(HomeContract.Event.OnClickProfile(userId = 1L))
                    }
                )
            }
        }
        
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "환영합니다! 🎉",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kotlin Multiplatform 프로젝트입니다.\n클린 아키텍처와 Compose를 사용합니다.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun NavigationCard(
    title: String,
    description: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineLarge
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