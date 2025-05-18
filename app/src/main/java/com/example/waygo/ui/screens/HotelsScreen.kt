package com.example.waygo.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.waygo.R
import com.example.waygo.ui.screens.BookScreen
import com.example.waygo.ui.screens.FormValidationScreen
import com.example.waygo.ui.screens.HomeHotel
import com.example.waygo.ui.screens.HotelDetailScreen
import com.example.waygo.ui.screens.HotelsSettingsScreen
import com.example.waygo.ui.screens.ReservationsScreen
import com.example.waygo.ui.screens.SubTaskScreen
import com.example.waygo.ui.screens.VersionScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(navController: NavController) {
    var showSettingsMenu by remember { mutableStateOf(false) }
    val nav = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.home_title))},
                actions = {
                    Box{
                        IconButton(onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                    }
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
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = nav,
                startDestination = "home"
            ) {
                /* ---------- Home scaffold with tabs ---------- */
                composable("home") { HomeHotel(nav) }

                /* ---------- Tabs rendered directly ---------- */
                composable("book")              { BookScreen(nav) }
                composable("my_reservations")   { ReservationsScreen() }
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
                composable("settings")  { HotelsSettingsScreen(nav) }
                composable("version")   { VersionScreen(nav) }
                composable("formValidation") { FormValidationScreen(nav) }
                composable(
                    "subtasks/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                ) { SubTaskScreen(nav) }
            }
        }
    }

}
