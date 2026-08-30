package com.example.collisionengine.ui.research

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    viewModel: ResearchViewModel,
    onNavigateBack: () -> Unit,
    onFindCollisions: (String) -> Unit
) {
    val queryText by viewModel.queryText.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Research Collision") },
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
                text = "Who has already worked on a version of your problem?",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Describe your problem, project, or domain naturally. Campus Connect will analyze your description to find overlapping methods, technologies, and fields.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = queryText,
                onValueChange = { viewModel.onQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Takes up available space naturally
                placeholder = {
                    Text("Example:\nI'm building an edge AI computer vision system using Raspberry Pi and YOLO. I'm struggling with inference speed.")
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                shape = MaterialTheme.shapes.medium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { onFindCollisions(queryText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = queryText.isNotBlank(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Find Collisions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
