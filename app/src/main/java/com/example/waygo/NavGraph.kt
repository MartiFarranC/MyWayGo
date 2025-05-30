package com.example.waygo

import android.util.Log // Import Log
import androidx.compose.material3.CircularProgressIndicator // Import for loading state
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf // Import derivedStateOf
import androidx.compose.ui.Alignment // Import Alignment
import androidx.compose.ui.Modifier // Import Modifier
import androidx.compose.foundation.layout.Box // Import Box
import androidx.compose.foundation.layout.fillMaxSize // Import fillMaxSize
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument // Import navArgument
import androidx.navigation.NavType // Import NavType
import com.example.waygo.data.local.entity.UserEntity
import com.example.waygo.ui.screens.Content1
import com.example.waygo.ui.screens.HomeScreenMenu
import com.example.waygo.ui.screens.LoginScreen
import com.example.waygo.ui.screens.ProfileScreen
import com.example.waygo.ui.screens.RegisterScreen
import com.example.waygo.ui.screens.TermConditionsScreen
import com.example.waygo.ui.screens.AboutScreen
import com.example.waygo.ui.screens.CostsScreen
import com.example.waygo.ui.screens.GalleryScreenWrapper
import com.example.waygo.ui.screens.SettingsScreen
import com.example.waygo.ui.screens.HelpScreen
import com.example.waygo.ui.screens.PasswordRecoveryScreen
import com.example.waygo.ui.screens.HomeScreen
import com.example.waygo.ui.screens.HotelsScreen
import com.example.waygo.ui.viewmodel.RegisterViewModel
import com.example.waygo.ui.viewmodel.TripViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG_NAV_GRAPH = "NavGraph" // Consistent TAG for this file

@Composable
fun NavGraph(navController: NavHostController, tripViewModel: TripViewModel) {
    val userDao = AppDatabase.getInstance(navController.context).userDao()
    val registerViewModel = remember { RegisterViewModel() } // Ensure RegisterViewModel is appropriate here, might need DI

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userDao) }
        composable("home") {
            HomeScreenMenu(navController) { innerPadding ->
                Content1(navController, innerPadding, tripViewModel)
            }
        }
        composable("gallery") {
            GalleryScreenWrapper(viewModel = tripViewModel, tripId = 1, onBack = { /*...*/ })
        }

        composable("profile") {
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            var user by remember { mutableStateOf<UserEntity?>(null) }

            if (userId == null) {
                // If userId is null, navigate to login and pop profile from back stack
                LaunchedEffect(Unit) { // Use Unit key to run once
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            } else {
                LaunchedEffect(userId) {
                    try {
                        user = withContext(Dispatchers.IO) {
                            userDao.getUserById(userId)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG_NAV_GRAPH, "Error loading user profile: ${e.message}", e)
                        user = null // Handle error gracefully
                    }
                }
            }

            user?.let {
                ProfileScreen(navController, it, registerViewModel)
            } ?: Text(text = stringResource(id = R.string.loading))
        }

        composable("register") { RegisterScreen(navController, userDao, registerViewModel) }
        composable("terms") { TermConditionsScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("cost") { CostsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("help") { HelpScreen(navController) }
        composable("password_recovery") { PasswordRecoveryScreen(navController = navController) }
        composable("hotels") { HotelsScreen(navController) }
        composable("hotelsmenu") { HomeScreen(navController) }

        // --- Gallery Screen Composable ---
        composable(
            route = "gallery/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId")

            val trips by tripViewModel.trips.collectAsState()

            val tripState by remember(trips, tripId) {
                derivedStateOf {
                    if (tripId == null) {
                        null
                    } else {
                        trips.find { it.id == tripId }
                    }
                }
            }

            LaunchedEffect(tripState) {
                if (tripState != null) {
                    tripState?.images?.forEachIndexed { index, uri ->
                        Log.d(TAG_NAV_GRAPH, "  Image $index URI: $uri")
                    }
                } else {
                    Log.d(TAG_NAV_GRAPH, "GalleryScreen's trip object is null or loading (tripId: $tripId).")
                }
            }

            //
            tripState?.let { currentTrip ->
                GalleryScreenWrapper(
                    viewModel = tripViewModel,
                    tripId = tripId ?: return@composable,  // Make sure tripId is non-null
                    onBack = { navController.popBackStack() }
                )
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                    Text(text = stringResource(id = R.string.loading))

                }
            }
        }
    }
}