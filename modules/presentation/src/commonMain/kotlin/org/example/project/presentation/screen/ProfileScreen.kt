package org.example.project.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.navigation.actions.MainNavigationActions
import org.example.project.presentation.navigation.arguments.ProfileArgument

/**
 * 프로필 스크린
 * - 사용자 프로필 조회/편집
 * - 개인 정보 관리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    argument: ProfileArgument?,
    navigationActions: MainNavigationActions
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (argument?.isEditMode == true) "프로필 편집" else "프로필",
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

        if (argument == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("프로필 정보를 불러올 수 없습니다")
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileCard(argument)
        }
    }
}

@Composable
private fun ProfileCard(argument: ProfileArgument) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "👤",
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "사용자 ${argument.userId}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (argument.isEditMode) "편집 모드" else "조회 모드",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}