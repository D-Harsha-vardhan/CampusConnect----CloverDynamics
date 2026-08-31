package com.example.collisionengine.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.example.collisionengine.ui.theme.PrimaryBlue
import com.example.collisionengine.ui.theme.TextPrimaryLight

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isVisible = true
        delay(2500L) // slightly longer delay to show off animation
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryBlue, Color.White)
                )
            )
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                initialOffsetY = { 50 },
                animationSpec = tween(1000)
            ),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Campus Connect",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Monospace
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        }
        
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 500)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = "Find the people who've been\nwhere you're going.",
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryBlue,
                textAlign = TextAlign.Center
            )
        }
    }
}
