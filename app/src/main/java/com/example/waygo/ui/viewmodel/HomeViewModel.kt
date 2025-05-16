package com.example.waygo.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waygo.domain.model.Task
import com.example.waygo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Estado mutable para mantener el listado de tareas
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        loadTasksOld()
    }

    private fun loadTasksOld() {
        _tasks.clear()
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.addAll(repository.getTasks())
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            // Fetch the tasks on IO thread
            val tasksFromDb = withContext(Dispatchers.IO) { repository.getTasks() }
            // Update state on main thread
            _tasks.clear()
            _tasks.addAll(tasksFromDb)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
            loadTasks()
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            loadTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks()
        }
    }
}