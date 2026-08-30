package com.example.collisionengine.ui.conversation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}
