package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinproject.app.generated.resources.Res
import kotlinproject.app.generated.resources.notosanskr_regular
import org.example.project.theme.AppTheme
import org.jetbrains.compose.resources.Font

/**
 * 간단한 테스트 화면
 * Koin 없이 순수 Compose만 사용
 * AppTheme를 사용하여 모든 텍스트에 자동으로 폰트 적용
 */
@Composable
fun Web() {
    var count by remember { mutableStateOf(0) }
    val notoSansKR = FontFamily(
        Font(Res.font.notosanskr_regular, FontWeight.Normal)
    )
    
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Kotlin Multiplatform WASM 테스트",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = notoSansKR
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "플랫폼: ${getPlatform().name}",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = notoSansKR
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "카운트: $count",
                style = MaterialTheme.typography.displayMedium,
                fontFamily = notoSansKR
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = { count++ }) {
                Text(
                    text = "증가",
                    fontFamily = notoSansKR
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = { count = 0 }) {
                Text(
                    text = "초기화",
                    fontFamily = notoSansKR
                )
            }
        }
    }
}
