package com.example.collisionengine.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.collisionengine.ui.components.StandardCard
import com.example.collisionengine.ui.components.bouncyClickable
import com.example.collisionengine.ui.theme.*
import com.example.collisionengine.data.state.GlobalProfileState
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {
    var isLiked1 by remember { mutableStateOf(true) }
    var isLiked2 by remember { mutableStateOf(false) }

    val userName by GlobalProfileState.name.collectAsState()
    val userRole by GlobalProfileState.role.collectAsState()
    val userBio by GlobalProfileState.bio.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var showConnectionsDialog by remember { mutableStateOf(false) }
    var showPapersDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section with Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // Banner Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(PrimaryBlue, SecondaryBlue)
                            )
                        )
                ) {
                    // Top App Bar Icons overlaid on banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        IconButton(
                            onClick = { /* Settings */ },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                    }
                }

                // Profile Avatar overlapping banner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-20).dp)
                        .size(120.dp)
                        .shadow(16.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, BackgroundLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(64.dp),
                            tint = PrimaryBlue
                        )
                    }
                }
            }

            // User Info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userRole,
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userBio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = PrimaryBlue.copy(alpha = 0.1f))
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier.clickable { showConnectionsDialog = true }) {
                        ProfileStat(count = "24", label = "Connections")
                    }
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = BackgroundLight
                    )
                    Box(modifier = Modifier.clickable { showPapersDialog = true }) {
                        ProfileStat(count = "5", label = "Papers")
                    }
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = BackgroundLight
                    )
                    ProfileStat(count = "12k", label = "Views")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Edit Profile Button
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .bouncyClickable { },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Activity Section
            Text(
                text = "My Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActivityTimelineItem(
                title = "Optimizing LLM Inference",
                subtitle = "Published • 2 days ago",
                icon = Icons.Filled.Edit,
                tags = listOf("AI", "Machine Learning", "Research"),
                isLast = false
            )

            ActivityTimelineItem(
                title = "Software Eng Prep Group",
                subtitle = "Joined • 1 week ago",
                icon = Icons.Filled.Person,
                tags = listOf("Mock Interviews", "Placement", "FAANG"),
                isLast = true
            )

            Spacer(modifier = Modifier.height(120.dp)) // Padding for bottom nav
        }
    }

    if (showEditDialog) {
        var editName by remember { mutableStateOf(userName) }
        var editRole by remember { mutableStateOf(userRole) }
        var editBio by remember { mutableStateOf(userBio) }
        
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") }
                    )
                    OutlinedTextField(
                        value = editRole,
                        onValueChange = { editRole = it },
                        label = { Text("Role") }
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio") },
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    GlobalProfileState.name.value = editName
                    GlobalProfileState.role.value = editRole
                    GlobalProfileState.bio.value = editBio
                    showEditDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showConnectionsDialog) {
        AlertDialog(
            onDismissRequest = { showConnectionsDialog = false },
            title = { Text("Connections (24)") },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    items(listOf("Aditya Kulkarni", "Sarah J.", "Emily Chen", "Rajesh Kumar", "Michael Smith")) { name ->
                        Text(text = name, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.bodyLarge)
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showConnectionsDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showPapersDialog) {
        AlertDialog(
            onDismissRequest = { showPapersDialog = false },
            title = { Text("Papers (5)") },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    items(listOf("Edge AI and Computer Vision", "Distributed Systems in Healthcare", "IoT for Smart Parking", "LLMs for Code Generation", "Graph Neural Networks")) { title ->
                        Text(text = title, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.bodyLarge)
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPapersDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ActivityTimelineItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tags: List<String>,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Timeline Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape, spotColor = PrimaryBlue.copy(alpha = 0.2f))
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
            if (!isLast) {
                Divider(
                    color = PrimaryBlue.copy(alpha = 0.2f),
                    modifier = Modifier
                        .width(2.dp)
                        .height(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = PrimaryBlue.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                Spacer(modifier = Modifier.height(12.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryLight
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondaryLight
        )
    }
}
