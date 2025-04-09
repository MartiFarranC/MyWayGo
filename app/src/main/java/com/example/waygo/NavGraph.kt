package com.example.waygo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.waygo.ui.screens.Content1
import com.example.waygo.ui.screens.HomeScreenMenu
import com.example.waygo.ui.screens.LoginScreen
import com.example.waygo.ui.screens.ProfileScreen
import com.example.waygo.ui.screens.RegisterScreen
import com.example.waygo.ui.screens.TermConditionsScreen
import com.example.waygo.ui.screens.AboutScreen
import com.example.waygo.ui.screens.CostsScreen
import com.example.waygo.ui.screens.SettingsScreen
import com.example.waygo.ui.screens.ToDoListScreen
import com.example.waygo.ui.screens.TravelCreatorScreen
import com.example.waygo.ui.screens.TravelScreen1
import com.example.waygo.ui.screens.TravelScreen2
import com.example.waygo.ui.screens.HelpScreen
import com.example.waygo.viewmodel.TripViewModel


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") {
            val tripViewModel = TripViewModel()
            HomeScreenMenu(navController) { innerPadding ->
                Content1(navController, innerPadding, tripViewModel)
            }
        }
        composable("profile") { ProfileScreen(navController) }
        composable("profileMenu") { ProfileScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("terms") { TermConditionsScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("creator") { TravelCreatorScreen(navController) }
        composable("cost") { CostsScreen(navController) }
        composable("todo") { ToDoListScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("travel1") { TravelScreen1(navController) }
        composable("travel2") { TravelScreen2(navController) }
        composable("help") { HelpScreen(navController) }
    }
}