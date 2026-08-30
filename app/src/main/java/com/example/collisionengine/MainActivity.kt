package com.example.collisionengine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.collisionengine.ui.navigation.AppNavigation
import com.example.collisionengine.ui.theme.CollisionEngineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollisionEngineTheme {
                AppNavigation()
            }
        }
    }
}
