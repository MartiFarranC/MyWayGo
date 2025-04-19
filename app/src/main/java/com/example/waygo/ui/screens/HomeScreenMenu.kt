package com.example.waygo.ui.screens

//import per a solucionar l'error unresolved reference 'string'
import android.icu.text.CaseMap.Title
import com.example.waygo.R

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMenu(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(onClick = {
                        navController.navigate("cost") {
                            popUpTo("cost") { inclusive = true }
                        }
                    }) {
                        Text(text = stringResource(id = R.string.cost_estimation))
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(onClick = {
                        navController.navigate("todo") {
                            popUpTo("todo") { inclusive = true }
                        }
                    }) {
                        Text(text = stringResource(id = R.string.todo_list))
                    }

                    Spacer(Modifier.height(12.dp))

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(text = stringResource(id = R.string.configuration), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(id = R.string.settings)) },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        badge = { Text("20") }, // Placeholder
                        onClick = {
                            navController.navigate("settings") {
                                popUpTo("settings") { inclusive = true } }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(id = R.string.help_and_feedback)) },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                        onClick = {
                            navController.navigate("help") {
                                popUpTo("help") { inclusive = true } }
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Button(onClick = {
                        navController.navigate("terms") {
                            popUpTo("terms") { inclusive = true }
                        }
                    }) {
                        Text(text = stringResource(id = R.string.terms_and_conditions))
                    }

                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        navController.navigate("about")
                    }) {
                        Text(text = stringResource(id = R.string.about_us))
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}