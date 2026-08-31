package com.example.collisionengine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collisionengine.ui.home.HomeScreen
import com.example.collisionengine.ui.splash.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToResearch = { navController.navigate(Screen.Research.route) },
                onNavigateToPlacement = { navController.navigate(Screen.Placement.route) }
            )
        }
        composable(route = Screen.Research.route) {
            val viewModel: com.example.collisionengine.ui.research.ResearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            com.example.collisionengine.ui.research.ResearchScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onFindCollisions = { query -> 
                    navController.navigate(Screen.Results.createRoute("Research", query))
                }
            )
        }
        composable(route = Screen.Placement.route) {
            val viewModel: com.example.collisionengine.ui.placement.PlacementViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            com.example.collisionengine.ui.placement.PlacementScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onFindCollisions = { query -> 
                    navController.navigate(Screen.Results.createRoute("Placement", query))
                }
            )
        }
        composable(
            route = Screen.Results.route,
            arguments = listOf(
                androidx.navigation.navArgument("type") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("query") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""
            
            // Note: In Compose, the Application context can be retrieved via LocalContext
            val factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
                androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
            )
            val viewModel: com.example.collisionengine.ui.results.ResultsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
            
            com.example.collisionengine.ui.results.ResultsScreen(
                query = query,
                searchType = type,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onMatchSelected = { match ->
                    navController.navigate(
                        Screen.Explanation.createRoute(
                            name = match.personName,
                            role = match.roleTitle,
                            reason = match.matchReason,
                            score = match.score
                        )
                    )
                }
            )
        }
        
        composable(
            route = Screen.Explanation.route,
            arguments = listOf(
                androidx.navigation.navArgument("name") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("role") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("reason") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("score") { type = androidx.navigation.NavType.IntType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val reason = backStackEntry.arguments?.getString("reason") ?: ""
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            
            com.example.collisionengine.ui.explanation.ExplanationScreen(
                name = name,
                role = role,
                reason = reason,
                score = score,
                onNavigateBack = { navController.popBackStack() },
                onStartConversation = {
                    navController.navigate(Screen.Conversation.createRoute(name, reason))
                }
            )
        }
        
        composable(
            route = Screen.Conversation.route,
            arguments = listOf(
                androidx.navigation.navArgument("name") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("reason") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
            val reason = backStackEntry.arguments?.getString("reason") ?: "Unknown reason"
            val viewModel: com.example.collisionengine.ui.conversation.ConversationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            com.example.collisionengine.ui.conversation.ConversationScreen(
                name = name,
                reason = reason,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { navController.navigate(Screen.Chat.createRoute(name)) }
            )
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(androidx.navigation.navArgument("name") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
            com.example.collisionengine.ui.chat.ChatScreen(
                name = name,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
