package com.example.collisionengine.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collisionengine.ui.components.*
import com.example.collisionengine.ui.theme.*
import com.example.collisionengine.data.state.GlobalProfileState

@Composable
fun HomeScreen(
    onNavigateToResearch: () -> Unit,
    onNavigateToPlacement: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Collisions") }
    var isPlacementLiked by remember { mutableStateOf(false) }
    
    var isVisible by remember { mutableStateOf(false) }
    val userName by GlobalProfileState.name.collectAsState()
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val categories = listOf("All Collisions", "Research", "Placement", "Campus Insights")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Top Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientTop, BackgroundLight)
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // 1. Header
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300))
            ) {
                TopHeader(userName = userName, onNotificationClick = onNavigateToNotifications)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 2. Search
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 100)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 100))
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // 3. Quick Actions
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 150)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 150))
            ) {
                Column {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryLight,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            QuickActionCard(icon = Icons.Filled.Group, label = "Peers", onClick = {})
                        }
                        item {
                            QuickActionCard(icon = Icons.Filled.MenuBook, label = "Papers", onClick = onNavigateToResearch)
                        }
                        item {
                            QuickActionCard(icon = Icons.Filled.Code, label = "Prep", onClick = onNavigateToPlacement)
                        }
                        item {
                            QuickActionCard(icon = Icons.Filled.Event, label = "Insights", onClick = {})
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Custom AI Search Card
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 200)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 200))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clickable { onNavigateToResearch() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue)
                ) {
                    Box(
                        modifier = Modifier.background(
                            Brush.verticalGradient(colors = listOf(SecondaryBlue, PrimaryBlue))
                        )
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = "What are you trying to solve?",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Outlined.Search, contentDescription = "Search", tint = Color.White)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Describe your problem, project, or goal...",
                                        color = Color.White.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.Mic, contentDescription = "Mic", tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
        }
    }
}
