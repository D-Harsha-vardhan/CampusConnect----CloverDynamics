package com.example.collisionengine.ui.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collisionengine.data.network.DatabricksClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.collisionengine.data.model.ChatMessage
import kotlinx.coroutines.launch

class ResearchViewModel : ViewModel() {
    private val _queryText = MutableStateFlow("")
    val queryText: StateFlow<String> = _queryText.asStateFlow()
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onQueryChanged(newText: String) {
        _queryText.value = newText
    }

    fun askDatabricks() {
        val query = _queryText.value
        if (query.isBlank()) return
        
        val userMsg = ChatMessage(text = query, isUser = true)
        _messages.value = _messages.value + userMsg
        
        _isLoading.value = true
        _queryText.value = "" // clear input
        
        viewModelScope.launch {
            val result = DatabricksClient.askGenie(query)
            
            // Add AI response, making the first one a TopMatch for demonstration if it has results
            val isTopMatchMock = _messages.value.size <= 1 && result.length > 20
            val aiMsg = ChatMessage(text = result, isUser = false, isTopMatch = isTopMatchMock)
            _messages.value = _messages.value + aiMsg
            _isLoading.value = false
        }
    }
}
