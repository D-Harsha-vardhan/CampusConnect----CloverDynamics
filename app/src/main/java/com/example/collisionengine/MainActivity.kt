package com.example.collisionengine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.collisionengine.ui.navigation.AppNavigation
import com.example.collisionengine.ui.theme.CollisionEngineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Load the JSON assets into memory
        com.example.collisionengine.data.network.LocalDatasetClient.init(applicationContext)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CollisionEngineTheme {
                AppNavigation()
            }
        }
    }
}
