package com.example.waygo.ui.view



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.waygo.domain.model.SubTask
import com.example.waygo.ui.viewmodel.SubTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTaskScreenSimple(
    navController: NavController,
    viewModel: SubTaskViewModel = hiltViewModel()
) {
    val subTasks = viewModel.subTasks

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Subtareas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack, contentDescription = "Volver")
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
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(subTasks) { subTask ->
                    SubTaskItem(
                        subTask = subTask,
                        onEdit = {},
                        onDelete = { viewModel.deleteSubTask(subTask.id) }
                    )
                }
            }
            // Botón para añadir una subtarea de ejemplo
            Button(
                onClick = {
                    // Se añade una subtarea de ejemplo; en una app real pedirías datos
                    viewModel.addSubTask(
                        SubTask(
                            parentTaskId = viewModel.taskId,
                            title = "Nueva Subtarea",
                            description = "Descripción de la subtarea"
                        )
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Añadir Subtarea")
            }
        }
    }
}

@Composable
fun SubTaskSimpleItem(subTask: SubTask, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = subTask.title,
            modifier = Modifier
                .weight(1f)
                .clickable { /* Podrías abrir detalle o edición */ }
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack, // Usa el ícono que prefieras para eliminar
                contentDescription = "Eliminar Subtarea"
            )
        }
    }
}