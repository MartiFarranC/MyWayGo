package com.example.waygo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.waygo.domain.model.Task
import com.example.waygo.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Tareas") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)) {

            // Lista de tareas
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(task = task, onClick = {
                        // Navegar a la pantalla de subtareas pasando el taskId
                        navController.navigate("subtasks/${task.id}")
                    })
                }
            }

            // Botón para añadir una tarea de ejemplo
            Button(
                onClick = {
                    // Se añade una tarea de ejemplo; en una app real pedirías datos
                    viewModel.addTask(Task(title = "Nueva Tarea", description = "Descripción de la tarea"))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Añadir Tarea")
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    // Representa cada tarea (puedes personalizar el diseño)
    Text(
        text = task.title,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
            .padding(8.dp)
    )
}
