package com.example.waygo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.waygo.database.AppDatabase
import com.example.waygo.entity.UserEntity
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
import com.example.waygo.ui.screens.PasswordRecoveryScreen
import com.example.waygo.ui.screens.SecondRegisterScreen
import com.example.waygo.viewmodel.RegisterViewModel
import com.example.waygo.viewmodel.TripViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun NavGraph(navController: NavHostController, tripViewModel: TripViewModel) {
    val userDao = AppDatabase.getInstance(navController.context).userDao()
    val registerViewModel = remember { RegisterViewModel() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userDao) }
        composable("home") {
            HomeScreenMenu(navController) { innerPadding ->
                Content1(navController, innerPadding, tripViewModel)
            }
        }

        composable("profile") {
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            var user by remember { mutableStateOf<UserEntity?>(null) }

            if (userId == null) {
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            } else {
                LaunchedEffect(userId) {
                    try {
                        user = withContext(Dispatchers.IO) {
                            userDao.getUserById(userId)
                        }
                    } catch (e: Exception) {
                        user = null
                    }
                }
            }

            user?.let {
                ProfileScreen(navController, it, registerViewModel)
            } ?: Text("Unable to load profile. Please try again.") //TODO: Traducció
        }

        composable("register") { RegisterScreen(navController, userDao, registerViewModel) }
        composable("terms") { TermConditionsScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("cost") { CostsScreen(navController) }
        composable("todo") { ToDoListScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("help") { HelpScreen(navController) }
        composable("second_register") { SecondRegisterScreen(navController, userDao, registerViewModel) }
        composable("password_recovery") { PasswordRecoveryScreen(navController = navController) }
    }
}