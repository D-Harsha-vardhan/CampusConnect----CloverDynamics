package com.example.collisionengine.ui.conversation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    name: String,
    reason: String,
    viewModel: ConversationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChat: () -> Unit = {}
) {
    val message by viewModel.suggestedMessage.collectAsState()
    val context = LocalContext.current
    
    // States: "NONE", "PENDING", "ACCEPTED"
    var connectionState by remember { mutableStateOf("NONE") }

    LaunchedEffect(name, reason) {
        viewModel.generateMessage(name, reason)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Connect with $name") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(
                text = "AI Suggested Icebreaker",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You can edit this message before sending it.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { viewModel.updateMessage(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = MaterialTheme.shapes.medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Choose a Platform",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // LinkedIn Button
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/search/results/people/?keywords=${Uri.encode(name)}"))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Find on LinkedIn")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Email Button
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_SUBJECT, "Connecting via Campus Connect")
                        putExtra(Intent.EXTRA_TEXT, message)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send via Email")
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            // Connection Logic
            if (connectionState == "NONE") {
                Button(
                    onClick = { 
                        viewModel.sendMessageToSupabase()
                        connectionState = "PENDING" 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send Connect Request")
                }
            } else if (connectionState == "PENDING") {
                LaunchedEffect(Unit) {
                    delay(2000) // Simulate other user accepting
                    connectionState = "ACCEPTED"
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = false
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request Pending...")
                }
            } else if (connectionState == "ACCEPTED") {
                Button(
                    onClick = onNavigateToChat,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request Accepted! Open Chat")
                }
            }
        }
    }
}
