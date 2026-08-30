package com.example.collisionengine.ui.placement

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlacementViewModel : ViewModel() {
    private val _queryText = MutableStateFlow("")
    val queryText: StateFlow<String> = _queryText.asStateFlow()

    fun onQueryChanged(newText: String) {
        _queryText.value = newText
    }

    // We will handle the search action fully in Phase 6 (Mock Collision Engine)
}
