package com.example.collisionengine.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    @SerialName("id")
    val id: String = "",
    @SerialName("connection_id")
    val connectionId: String = "", // Currently a UUID in DB, but can map to String
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("content")
    val content: String,
    @SerialName("created_at")
    val createdAt: String = ""
)
