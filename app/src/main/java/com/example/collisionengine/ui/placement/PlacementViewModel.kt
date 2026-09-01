package com.example.collisionengine.ui.placement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collisionengine.data.network.DatabricksClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacementViewModel : ViewModel() {
    private val _queryText = MutableStateFlow("")
    val queryText: StateFlow<String> = _queryText.asStateFlow()
    
    private val _responseText = MutableStateFlow<String?>(null)
    val responseText: StateFlow<String?> = _responseText.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onQueryChanged(newText: String) {
        _queryText.value = newText
    }

    fun askDatabricks() {
        val query = _queryText.value
        if (query.isBlank()) return
        
        _isLoading.value = true
        _responseText.value = null
        
        viewModelScope.launch {
            val result = DatabricksClient.askGenie(query)
            _responseText.value = result
            _isLoading.value = false
        }
    }
}
