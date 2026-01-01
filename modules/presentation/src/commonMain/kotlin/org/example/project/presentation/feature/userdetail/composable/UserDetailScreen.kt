package org.example.project.presentation.feature.userdetail.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.User
import org.example.project.presentation.feature.userdetail.UserDetailContract
import org.example.project.presentation.core.SIDE_EFFECTS_KEY

/**
 * 사용자 상세 스크린
 * - 특정 사용자의 상세 정보 표시
 * - 사용자 정보 편집 기능 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    state: UserDetailContract.State,
    effectFlow: Flow<UserDetailContract.Effect>?,
    onEventSent: (event: UserDetailContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: UserDetailContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is UserDetailContract.Effect.Navigation -> onNavigationRequested(effect)
                is UserDetailContract.Effect.ShowError -> { /* TODO: Show Snackbar */ }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = state.user?.name ?: "사용자 상세",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onEventSent(UserDetailContract.Event.OnBackClick) }
                ) {
                    Text("←")
                }
            }
        )
        
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        // 사용자 정보가 없는 경우 (로딩 중이 아닐 때)
        if (state.user == null && !state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌",
                        style = MaterialTheme.typography.displayMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = state.error ?: "사용자 정보를 불러올 수 없습니다",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            return
        }

        state.user?.let { user ->
            // 사용자 상세 정보
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UserInfoCard(user)

                ActionsCard(onEditClick = { onEventSent(UserDetailContract.Event.OnEditProfileClick) })
            }
        }
    }
}

@Composable
private fun UserInfoCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "사용자 정보",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                label = "사용자 ID",
                value = user.id
            )

            InfoRow(
                label = "사용자명",
                value = user.name
            )
            
            InfoRow(
                label = "이메일",
                value = user.email
            )
        }
    }
}

@Composable
private fun ActionsCard(
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "작업",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("프로필 편집")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}