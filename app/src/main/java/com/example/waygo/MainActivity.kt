package com.example.waygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.example.waygo.ui.theme.WayGoTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.waygo.viewmodel.TripViewModel
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    private val tripViewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WayGoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(tripViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(tripViewModel: TripViewModel) {
    val navController = rememberNavController()
    NavGraph(navController = navController, tripViewModel = tripViewModel)
}
