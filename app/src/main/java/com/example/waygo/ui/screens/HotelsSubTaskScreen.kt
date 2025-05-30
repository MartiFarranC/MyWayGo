package com.example.waygo.ui.screens

import com.example.waygo.domain.model.SubTask
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.waygo.R
import com.example.waygo.ui.viewmodel.SubTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTaskScreen(
    navController: NavController,
    viewModel: SubTaskViewModel = hiltViewModel()
) {
    val subTasks = viewModel.subTasks

    var showDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var currentSubTaskId by remember { mutableStateOf(0) }
    var subTaskTitle by remember { mutableStateOf("") }
    var subTaskDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.edit_subtask))},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isEditing = false
                    subTaskTitle = ""
                    subTaskDescription = ""
                    showDialog = true
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add SubTask")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(subTasks) { subTask ->
                    SubTaskItem(
                        subTask = subTask,
                        onEdit = {
                            isEditing = true
                            currentSubTaskId = subTask.id
                            subTaskTitle = subTask.title
                            subTaskDescription = subTask.description
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteSubTask(subTask.id) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isEditing) stringResource(id = R.string.edit_subtask) else stringResource(id = R.string.new_subtask)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = subTaskTitle,
                        onValueChange = { subTaskTitle = it },
                        label = { Text(stringResource(id = R.string.title)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = subTaskDescription,
                        onValueChange = { subTaskDescription = it },
                        label = { Text(stringResource(id = R.string.description)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isEditing) {
                            viewModel.updateSubTask(
                                SubTask(
                                    id = currentSubTaskId,
                                    parentTaskId = viewModel.taskId,
                                    title = subTaskTitle,
                                    description = subTaskDescription
                                )
                            )
                        } else {
                            viewModel.addSubTask(
                                SubTask(
                                    parentTaskId = viewModel.taskId,
                                    title = subTaskTitle,
                                    description = subTaskDescription
                                )
                            )
                        }
                        showDialog = false
                    }
                ) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun SubTaskItem(
    subTask: SubTask,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subTask.title,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Subtask"
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Subtask"
            )
        }
    }
}
