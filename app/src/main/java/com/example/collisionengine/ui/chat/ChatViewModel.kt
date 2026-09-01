package com.example.collisionengine.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collisionengine.data.models.ChatMessage
import com.example.collisionengine.data.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.decodeRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // For the hackathon, we will hardcode a mock connection ID and "my" user ID.
    // In production, these come from Supabase Auth and the connections table.
    private val mockConnectionId = "00000000-0000-0000-0000-000000000000"
    val myUserId = "user_me"

    init {
        fetchMessages()
        subscribeToRealtime()
    }

    private fun fetchMessages() {
        viewModelScope.launch {
            try {
                val fetchedMessages = SupabaseClient.client.from("messages")
                    .select {
                        // In a real app: eq("connection_id", mockConnectionId)
                    }
                    .decodeList<ChatMessage>()
                
                _messages.value = fetchedMessages
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error fetching messages", e)
            }
        }
    }

    private fun subscribeToRealtime() {
        viewModelScope.launch {
            try {
                val channel = SupabaseClient.client.channel("public:messages")
                
                // Listen for any inserts to the messages table
                val messageFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                    table = "messages"
                }

                channel.subscribe()

                messageFlow.collect { action ->
                    val newMessage = action.decodeRecord<ChatMessage>()
                    _messages.value = _messages.value + newMessage
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error subscribing to realtime", e)
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            try {
                val newMessage = ChatMessage(
                    // connectionId = mockConnectionId,
                    senderId = myUserId,
                    content = content
                )
                SupabaseClient.client.from("messages").insert(newMessage)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message", e)
            }
        }
    }
}
