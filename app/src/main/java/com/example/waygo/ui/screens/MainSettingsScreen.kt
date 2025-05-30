package com.example.waygo.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R
import java.util.*

fun updateAppLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("language", languageCode).apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf("") }

    val languages = listOf("English", "Catalan", "Spanish")
    val themes = listOf("Dark", "Light", "System Default")

    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var selectedLanguage by remember { mutableStateOf(
        when (prefs.getString("language", "en")) {
            "ca" -> "Catalan"
            "es" -> "Spanish"
            else -> "English"
        }
    ) }
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.settings))
        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.select_language)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = {
                            selectedLanguage = language
                            expanded = false

                            val languageCode = when (language) {
                                "Catalan" -> "ca"
                                "Spanish" -> "es"
                                else -> "en"
                            }
                            updateAppLanguage(context, languageCode)

                            // Reiniciar la pantalla
                            navController.navigate("settings") {
                                popUpTo("settings") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.enable_notifications))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded2,
            onExpandedChange = { expanded2 = !expanded2 }
        ) {
            OutlinedTextField(
                value = selectedTheme,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.select_theme)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded2)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = { expanded2 = false }
            ) {
                themes.forEach { theme ->
                    DropdownMenuItem(
                        text = { Text(theme) },
                        onClick = {
                            selectedTheme = theme
                            expanded2 = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
        }) {
            Text(text = stringResource(id = R.string.save_settings))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("home") {
                popUpTo("back") { inclusive = true }
            }
        }) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}