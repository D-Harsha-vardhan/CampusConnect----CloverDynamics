package com.example.collisionengine.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Research : Screen("research")
    object Placement : Screen("placement")
    
    // Pass query encoded to avoid breaking the navigation string
    object Results : Screen("results/{type}/{query}") {
        fun createRoute(type: String, query: String) = "results/$type/${android.net.Uri.encode(query)}"
    }
    
    object Explanation : Screen("explanation/{name}/{role}/{reason}/{score}") {
        fun createRoute(name: String, role: String, reason: String, score: Int): String {
            return "explanation/${android.net.Uri.encode(name)}/${android.net.Uri.encode(role)}/${android.net.Uri.encode(reason)}/$score"
        }
    }
    
    object Conversation : Screen("conversation/{name}/{reason}") {
        fun createRoute(name: String, reason: String): String {
            return "conversation/${android.net.Uri.encode(name)}/${android.net.Uri.encode(reason)}"
        }
    }

    object Chat : Screen("chat/{name}") {
        fun createRoute(name: String) = "chat/${android.net.Uri.encode(name)}"
    }
}
