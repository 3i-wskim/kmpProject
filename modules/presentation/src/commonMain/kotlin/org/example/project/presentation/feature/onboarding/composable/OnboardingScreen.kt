package org.example.project.presentation.feature.onboarding.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.core.SIDE_EFFECTS_KEY
import org.example.project.presentation.feature.onboarding.OnboardingContract

/**
 * 온보딩 스크린
 * - 앱 사용법 안내
 * - 첫 사용자를 위한 가이드
 */
@Composable
fun OnboardingScreen(
    state: OnboardingContract.State,
    effectFlow: Flow<OnboardingContract.Effect>?,
    onEventSent: (event: OnboardingContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: OnboardingContract.Effect.Navigation) -> Unit,
) {
    // Effect 처리 (Navigation)
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is OnboardingContract.Effect.Navigation -> onNavigationRequested(effect)
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
        Text(
            text = "🚀",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "환영합니다!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "KMP로 구축된 멀티플랫폼 앱입니다.\n클린 아키텍처를 기반으로 안정적이고 확장 가능한 구조를 제공합니다.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onEventSent(OnboardingContract.Event.OnStartClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("시작하기")
        }
    }
}