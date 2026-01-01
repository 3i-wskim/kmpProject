package org.example.project.presentation.feature.profile.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.User
import org.example.project.presentation.feature.profile.ProfileContract
import org.example.project.presentation.core.SIDE_EFFECTS_KEY

/**
 * 프로필 스크린
 * - 사용자 프로필 조회/편집
 * - 개인 정보 관리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileContract.State,
    effectFlow: Flow<ProfileContract.Effect>?,
    onEventSent: (event: ProfileContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: ProfileContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is ProfileContract.Effect.Navigation -> onNavigationRequested(effect)
                is ProfileContract.Effect.ShowToast -> { /* TODO: Show Toast */ }
                is ProfileContract.Effect.ShowError -> { /* TODO: Show Snackbar */ }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (state.isEditMode) "프로필 편집" else "프로필",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onEventSent(ProfileContract.Event.OnBackClick) }
                ) {
                    Text("←")
                }
            },
            actions = {
                if (state.isEditMode) {
                    TextButton(
                        onClick = { onEventSent(ProfileContract.Event.OnSaveClick) }
                    ) {
                        Text("저장")
                    }
                }
            }
        )

        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.user == null && !state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("프로필 정보를 불러올 수 없습니다")
            }
            return
        }

        state.user?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileCard(user, state.isEditMode)
            }
        }
    }
}

@Composable
private fun ProfileCard(user: User, isEditMode: Boolean) {
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
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isEditMode) "편집 모드" else "조회 모드",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}