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
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val categories = listOf("All Collisions", "Research", "Placement", "Events")

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
                TopHeader(onNotificationClick = onNavigateToNotifications)
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
                            QuickActionCard(icon = Icons.Filled.Event, label = "Events", onClick = {})
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 4. Category Tabs
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 200)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 200))
            ) {
                CategoryTabs(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 5. Featured Card
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 300)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 300))
            ) {
                Column {
                    Text(
                        text = "Featured Collision",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryLight,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    FeaturedCard(
                        title = "Research Collisions",
                        subtitle = "Find people working on problems similar to yours.",
                        tags = listOf("Machine Learning", "Remote", "Data"),
                        onClick = onNavigateToResearch
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 6. Suggested Connections
            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(300, delayMillis = 400)) + androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(300, delayMillis = 400))
            ) {
                Column {
                    Text(
                        text = "Suggested Connections",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryLight,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    StandardCard(
                        title = "Placement Collisions",
                        subtitle = "Find people who've been through the role you're preparing for.",
                        tags = listOf("Software Eng", "Interview", "Mock"),
                        isLiked = isPlacementLiked,
                        onLikeClick = { isPlacementLiked = !isPlacementLiked },
                        onClick = onNavigateToPlacement
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
        }
    }
}
