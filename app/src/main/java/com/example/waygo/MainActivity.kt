package com.example.waygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.waygo.ui.theme.WayGoTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.waygo.viewmodel.TripViewModel
import com.example.waygo.dao.TripDao
import com.example.waygo.database.AppDatabase
import com.example.waygo.viewmodel.TripViewModelFactory
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    private val tripViewModel: TripViewModel by lazy {
        val tripDao: TripDao = AppDatabase.getInstance(this).tripDao()
        val factory = TripViewModelFactory(tripDao)
        ViewModelProvider(this, factory).get(TripViewModel::class.java)
    }

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