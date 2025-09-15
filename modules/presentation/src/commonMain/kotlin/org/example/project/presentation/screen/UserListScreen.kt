package org.example.project.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.arguments.UserDetailArgument

/**
 * 사용자 목록 스크린
 * - 등록된 사용자 목록 표시
 * - 사용자 선택 시 상세 페이지로 이동
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navigationActions: MainNavigationActions
) {
    // 임시 사용자 데이터 (실제로는 ViewModel에서 관리)
    val users = remember {
        listOf(
            UserDetailArgument(userId = 1L, userName = "김철수"),
            UserDetailArgument(userId = 2L, userName = "이영희"),
            UserDetailArgument(userId = 3L, userName = "박민수"),
            UserDetailArgument(userId = 4L, userName = "최수진"),
            UserDetailArgument(userId = 5L, userName = "정우성")
        )
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
                    onClick = { navigationActions.navigateBack() }
                ) {
                    Text("←")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserCard(
                    user = user,
                    onClick = { navigationActions.navigateToUserDetail(user) }
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    user: UserDetailArgument,
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
                        text = user.userName?.first()?.toString()?.uppercase() ?: "?",
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
                    text = user.userName ?: "이름 없음",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ID: ${user.userId}",
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