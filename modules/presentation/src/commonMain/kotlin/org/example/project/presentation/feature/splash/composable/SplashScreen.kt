package org.example.project.presentation.feature.splash.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.core.SIDE_EFFECTS_KEY
import org.example.project.presentation.feature.splash.SplashContract

/**
 * 스플래시 스크린
 * - 앱 시작 시 표시되는 초기 화면
 * - 초기화 작업 완료 후 홈 화면으로 이동
 */
@Composable
fun SplashScreen(
    state: SplashContract.State,
    effectFlow: Flow<SplashContract.Effect>?,
    onEventSent: (event: SplashContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SplashContract.Effect.Navigation) -> Unit,
) {
    // 애니메이션 시작 시점에 이벤트 발생
    LaunchedEffect(Unit) {
        onEventSent(SplashContract.Event.OnAnimationEnd)
    }

    // Effect 처리 (Navigation)
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SplashContract.Effect.Navigation -> onNavigationRequested(effect)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(120.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "KMP",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Kotlin Multiplatform Project",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Clean Architecture + Compose",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(32.dp)
        )
    }
}