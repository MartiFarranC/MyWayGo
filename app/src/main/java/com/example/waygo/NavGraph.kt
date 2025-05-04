package com.example.waygo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.waygo.database.AppDatabase
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
import com.example.waygo.ui.screens.HelpScreen
import com.example.waygo.viewmodel.TripViewModel


@Composable
fun NavGraph(navController: NavHostController, tripViewModel: TripViewModel) {
    val userDao = AppDatabase.getInstance(navController.context).userDao()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userDao) }
        composable("home") {
            HomeScreenMenu(navController) { innerPadding ->
                Content1(navController, innerPadding, tripViewModel)
            }
        }
        composable("profile") { ProfileScreen(navController) }
        composable("profileMenu") { ProfileScreen(navController) }
        composable("register") { RegisterScreen(navController, userDao) }
        composable("terms") { TermConditionsScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("cost") { CostsScreen(navController) }
        composable("todo") { ToDoListScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("help") { HelpScreen(navController) }
    }
}