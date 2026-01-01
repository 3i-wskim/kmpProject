package org.example.project.presentation.feature.userlist.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.User
import org.example.project.presentation.feature.userlist.UserListContract
import org.example.project.presentation.core.SIDE_EFFECTS_KEY

/**
 * 사용자 목록 스크린
 * - 등록된 사용자 목록 표시
 * - 사용자 선택 시 상세 페이지로 이동
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    state: UserListContract.State,
    effectFlow: Flow<UserListContract.Effect>?,
    onEventSent: (event: UserListContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: UserListContract.Effect.Navigation) -> Unit,
) {
    // Effect 처리
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is UserListContract.Effect.Navigation -> onNavigationRequested(effect)
                is UserListContract.Effect.ShowError -> {
                    // TODO: Show Snackbar
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "사용자 목록",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onEventSent(UserListContract.Event.OnBackClick) }
                ) {
                    Text("←")
                }
            },
            actions = {
                IconButton(
                    onClick = { onEventSent(UserListContract.Event.RefreshUsers) }
                ) {
                    Text("↻")
                }
            }
        )
        
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.users) { user ->
                UserCard(
                    user = user,
                    onClick = { onEventSent(UserListContract.Event.OnUserClick(user)) }
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    user: User,
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
            // 사용자 아바타
            Card(
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.firstOrNull()?.toString()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ID: ${user.id}",
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