package com.example.collisionengine.ui.placement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.lazy.LazyColumn
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.example.collisionengine.ui.components.AnimatedWaveform
import com.example.collisionengine.ui.components.ChatBubble
import com.example.collisionengine.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacementScreen(
    viewModel: PlacementViewModel,
    onNavigateBack: () -> Unit,
    onFindCollisions: (String) -> Unit,
    onMatchClick: (com.example.collisionengine.data.model.ProfileMatch) -> Unit
) {
    val queryText by viewModel.queryText.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    androidx.compose.runtime.LaunchedEffect(messages.size, isLoading) {
        val total = listState.layoutInfo.totalItemsCount
        if (total > 0) {
            listState.animateScrollToItem(total - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("What if simulator", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFF0F172A) // Dark background for the AI vibe
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Waveform moved inside LazyColumn to avoid squishing
            
            
            // Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        // AI Waveform Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedWaveform(
                                modifier = Modifier.fillMaxSize(),
                                color = PrimaryBlue
                            )
                            
                            // Signal Active Badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(PrimaryBlue.copy(alpha = 0.2f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Genie active",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    if (messages.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Talk to Campus Connect AI",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "I'm your secure, supportive placement companion. Describe your target role or company naturally. I will analyze your description to find overlapping interview prep, timelines, and study groups among your peers.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.DarkGray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                    
                    items(messages) { message ->
                        ChatBubble(
                            message = message.text,
                            isUser = message.isUser
                        )
                        if (!message.isUser && message.topMatches.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            message.topMatches.forEach { match ->
                                com.example.collisionengine.ui.components.ProfileMatchCard(
                                    match = match,
                                    onClick = { onMatchClick(match) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    
                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = PrimaryBlue,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "What if simulator is thinking...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            
            // Chat Input Area
            val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
            val isKeyboardOpen = imeBottom > 0
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B))
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = if (isKeyboardOpen) 16.dp else 90.dp)
                    .imePadding()
            ) {
                val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
                OutlinedTextField(
                    value = queryText,
                    onValueChange = { viewModel.onQueryChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(200) // Wait for keyboard to open
                                    val total = listState.layoutInfo.totalItemsCount
                                    if (total > 0) {
                                        listState.animateScrollToItem(total - 1)
                                    }
                                }
                            }
                        },
                    placeholder = {
                        Text(
                            "Type your placement goals...",
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.askDatabricks() },
                            enabled = queryText.isNotBlank() && !isLoading
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send",
                                tint = if (queryText.isNotBlank()) PrimaryBlue else Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                )
            }
        }
    }
}
