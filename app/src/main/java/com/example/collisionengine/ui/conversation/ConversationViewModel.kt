package com.example.collisionengine.ui.conversation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from

class ConversationViewModel : ViewModel() {

    private val _suggestedMessage = MutableStateFlow("")
    val suggestedMessage: StateFlow<String> = _suggestedMessage.asStateFlow()

    fun generateMessage(name: String, reason: String) {
        // Simulated AI generation based on the reason
        val firstName = name.split(" ").firstOrNull() ?: name
        val msg = "Hi $firstName,\n\nI saw on Campus Connect that you $reason. I'm currently working on something very similar and struggling a bit. I'd love to connect and hear how you approached it!"
        _suggestedMessage.value = msg
    }

    fun updateMessage(newText: String) {
        _suggestedMessage.value = newText
    }

    fun sendMessageToSupabase() {
        val currentMessage = _suggestedMessage.value
        if (currentMessage.isBlank()) return
        
        viewModelScope.launch {
            try {
                val newMessage = com.example.collisionengine.data.models.ChatMessage(
                    senderId = "user_me",
                    content = currentMessage
                )
                com.example.collisionengine.data.network.SupabaseClient.client.from("messages").insert(newMessage)
            } catch (e: Exception) {
                android.util.Log.e("ConversationViewModel", "Error sending message", e)
            }
        }
    }
}
