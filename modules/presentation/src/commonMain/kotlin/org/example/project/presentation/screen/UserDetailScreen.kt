package org.example.project.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.arguments.UserDetailArgument
import org.example.project.presentation.navigation.arguments.ProfileArgument

/**
 * 사용자 상세 스크린
 * - 특정 사용자의 상세 정보 표시
 * - 사용자 정보 편집 기능 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    argument: UserDetailArgument?,
    navigationActions: MainNavigationActions
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = argument?.userName ?: "사용자 상세",
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

        // 사용자 정보가 없는 경우
        if (argument == null) {
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
                        text = "사용자 정보를 불러올 수 없습니다",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            return
        }

        // 사용자 상세 정보
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInfoCard(argument)

            ActionsCard(navigationActions, argument)
        }
    }
}

@Composable
private fun UserInfoCard(argument: UserDetailArgument) {
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
                value = argument.userId.toString()
            )

            argument.userName?.let { name ->
                InfoRow(
                    label = "사용자명",
                    value = name
                )
            }
        }
    }
}

@Composable
private fun ActionsCard(
    navigationActions: MainNavigationActions,
    argument: UserDetailArgument
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
                onClick = {
                    // 프로필 편집으로 이동
                    navigationActions.navigateToProfile(
                        ProfileArgument(
                            userId = argument.userId,
                            isEditMode = true
                        )
                    )
                },
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