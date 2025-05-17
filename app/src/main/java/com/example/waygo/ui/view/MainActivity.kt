package com.example.waygo.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waygo.ui.theme.WayGoTheme
import com.example.waygo.ui.view.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.waygo.R
import com.example.waygo.ui.view.BookScreen
import com.example.waygo.ui.view.FormValidationScreen
import com.example.waygo.ui.view.HomeHotel
import com.example.waygo.ui.view.HotelDetailScreen
import com.example.waygo.ui.view.ReservationsScreen
import com.example.waygo.ui.view.SubTaskScreen
import com.example.waygo.ui.view.VersionScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WayGoTheme {
                val nav = rememberNavController()

                NavHost(
                    navController = nav,
                    startDestination = "home"
                ) {
                    /* ---------- Home scaffold with tabs ---------- */
                    composable("home") { HomeHotel(nav) }

                    /* ---------- Tabs rendered directly ---------- */
                    composable("book")              { BookScreen(nav) }
                    composable("my_reservations")   { ReservationsScreen()}
                    composable("all_reservations")  { ReservationsScreen() }

                    /* ---------- Hotel detail ---------- */
                    composable(
                        route = "hotel/{hotelId}/{groupId}/{start}/{end}",
                        arguments = listOf(
                            navArgument("hotelId") { type = NavType.StringType },
                            navArgument("groupId") { type = NavType.StringType },
                            navArgument("start")   { type = NavType.StringType },
                            navArgument("end")     { type = NavType.StringType }
                        )
                    ) { back ->
                        HotelDetailScreen(
                            hotelId       = back.arguments!!.getString("hotelId")!!,
                            groupId       = back.arguments!!.getString("groupId")!!,
                            start         = back.arguments!!.getString("start")!!,
                            end           = back.arguments!!.getString("end")!!,
                            navController = nav
                        )
                    }

                    /* ---------- Other app sections ---------- */
                    composable("settings")  { SettingsScreen(nav) }
                    composable("version")   { VersionScreen(nav) }
                    composable("formValidation") { FormValidationScreen(nav) }
                    composable(
                        "subtasks/{taskId}",
                        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                    ) { SubTaskScreen(nav) }
                }



//                NavHost(navController = navController, startDestination = "home") {
//                    composable("home") {
//                        HomeHotel(navController)
//                    }
//                    composable("settings") {
//                        SettingsScreen(navController)
//                    }
//
//                    composable("version") {
//                        VersionScreen(navController)
//                    }
//                    composable("formValidation") {
//                        FormValidationScreen(navController)
//                    }
//                    composable(
//                        route = "subtasks/{taskId}",
//                        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
//                    ) {
//                        SubTaskScreen(navController = navController)
//                    }
//                }





            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var showSettingsMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) },
                actions = {
                    Box {
                        IconButton(onClick = { showSettingsMenu = !showSettingsMenu }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = { showSettingsMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = "Version Icon"
                                    )
                                },
                                text = { Text("Version") },
                                onClick = {
                                    showSettingsMenu = false
                                    navController.navigate("version")
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Settings Icon"
                                    )
                                },
                                text = { Text("Settings") },
                                onClick = {
                                    showSettingsMenu = false
                                    navController.navigate("settings")
                                }
                            )
                        }
                    }
                }
            )
        }

    ) { innerPadding ->
        // Home screen content
        Text(
            text = stringResource(id = R.string.home_title),
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WayGoTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(navController: NavController) {

    var showSettingsMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) },
                actions = {
                    Box {
                        IconButton(onClick = { showSettingsMenu = !showSettingsMenu }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = { showSettingsMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = "Child Screen 1"
                                    )
                                },
                                text = { Text("Version") },
                                onClick = {
                                    showSettingsMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // Home screen content
        Text(
            text = stringResource(id = R.string.home_title),
            modifier = Modifier.padding(innerPadding)
        )
    }
}
