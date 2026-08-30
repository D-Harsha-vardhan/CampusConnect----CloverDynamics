package com.example.collisionengine.ui.results

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.collisionengine.data.models.CollisionMatch
import com.example.collisionengine.data.repository.MockCollisionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = MockCollisionRepository(application)

    private val _matches = MutableStateFlow<List<CollisionMatch>>(emptyList())
    val matches: StateFlow<List<CollisionMatch>> = _matches.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun performSearch(query: String, searchType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Fake network / AI delay to simulate Databricks Genie processing
            delay(2000)
            
            val results = repository.findCollisions(query, searchType)
            _matches.value = results
            _isLoading.value = false
        }
    }
}
