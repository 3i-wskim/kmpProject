package org.example.project.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.di.getApplicationContainer
import org.example.project.presentation.viewmodel.UserViewModel

/**
 * 앱의 메인 네비게이션 화면
 *
 * 수동 DI를 사용하여 의존성을 주입받아 화면을 구성합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    // DI 컨테이너를 통해 ViewModel을 주입받습니다
    val diContainer = remember { getApplicationContainer() }
    val userViewModel = diContainer.userViewModel

    val uiState by userViewModel.uiState.collectAsState()
    val searchQuery by userViewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 상단 제목
        Text(
            text = "Kotlin Multiplatform Clean Architecture",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // 검색창
        OutlinedTextField(
            value = searchQuery,
            onValueChange = userViewModel::onSearchQueryChanged,
            label = { Text("사용자 검색") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // 사용자 추가 버튼
        Button(
            onClick = {
                userViewModel.addUser(
                    name = "새 사용자 ${kotlin.random.Random.nextInt(1000)}",
                    email = "user${kotlin.random.Random.nextInt(1000)}@example.com"
                )
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("새 사용자 추가")
        }

        // 에러 클리어 버튼 (에러가 있을 때만 표시)
        if (uiState.error != null) {
            Button(
                onClick = userViewModel::clearError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("에러 클리어")
            }
        }

        // 로딩 상태
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // 에러 상태
        uiState.error?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = "오류: $error",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 사용자 목록
        LazyColumn {
            items(uiState.users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        user.avatarUrl?.let { avatarUrl ->
                            Text(
                                text = "Avatar: $avatarUrl",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Text(
                            text = if (user.isActive) "활성" else "비활성",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (user.isActive) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                }
            }
        }

        // 빈 상태 (로컬 계산)
        val isEmptyState = uiState.users.isEmpty() && !uiState.isLoading
        if (isEmptyState) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotEmpty()) "검색 결과가 없습니다" else "사용자가 없습니다",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}