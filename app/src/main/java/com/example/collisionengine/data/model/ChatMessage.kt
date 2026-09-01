package com.example.collisionengine.data.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val isTopMatch: Boolean = false // If true, rendering will include the TopMatchCard
)
