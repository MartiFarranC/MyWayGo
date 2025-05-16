package com.example.waygo.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.waygo.R
import com.example.waygo.domain.model.Task
import com.example.waygo.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen3(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks
    var showSettingsMenu by remember { mutableStateOf(false) }
    var showTaskDialog by remember { mutableStateOf(false) }
    var isEditingTask by remember { mutableStateOf(false) }
    var currentTaskId by remember { mutableStateOf(0) }
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

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
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Icon"
                                    )
                                },
                                text = { Text("Form Validation") },
                                onClick = {
                                    showSettingsMenu = false
                                    navController.navigate("formValidation")
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Lista de tareas
            LazyColumn {
                items(tasks) { task ->
                    TaskItem2(
                        task = task,
                        onEdit = {
                            // Abre el modal en modo edición
                            isEditingTask = true
                            currentTaskId = task.id
                            taskTitle = task.title
                            taskDescription = task.description
                            showTaskDialog = true
                        },
                        onOpen = {
                            // Navega al listado de subtareas
                            navController.navigate("subtasks/${task.id}")
                        }
                    )
                }
            }
            // Botón para añadir una tarea de ejemplo (abre modal en modo creación)
            Button(
                onClick = {
                    isEditingTask = false
                    taskTitle = ""
                    taskDescription = ""
                    showTaskDialog = true
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Añadir Tarea")
            }
        }
    }

    // Modal para crear o editar tarea
    if (showTaskDialog) {
        AlertDialog(
            onDismissRequest = { showTaskDialog = false },
            title = { Text(text = if (isEditingTask) "Editar Tarea" else "Nueva Tarea") },
            text = {
                Column {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isEditingTask) {
                            viewModel.updateTask(
                                Task(
                                    id = currentTaskId,
                                    title = taskTitle,
                                    description = taskDescription
                                )
                            )
                        } else {
                            viewModel.addTask(
                                Task(
                                    title = taskTitle,
                                    description = taskDescription
                                )
                            )
                        }
                        showTaskDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showTaskDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TaskItem2(
    task: Task,
    onEdit: () -> Unit,
    onOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Muestra el título de la tarea
        Text(
            text = task.title,
            modifier = Modifier.weight(1f)
        )
        // Icono para editar (abre modal)
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Tarea"
            )
        }
        // Icono para abrir el listado de subtareas
        IconButton(onClick = onOpen) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Abrir Subtareas"
            )
        }
    }
}
