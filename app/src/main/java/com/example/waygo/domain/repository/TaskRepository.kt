package com.example.waygo.domain.repository

import com.example.waygo.domain.model.SubTask
import com.example.waygo.domain.model.Task

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun deleteTask(taskId: Int)
    suspend fun updateTask(task: Task)

    suspend fun getSubTasksForTask(taskId: Int): List<SubTask>
    suspend fun addSubTask(subTask: SubTask)
    suspend fun deleteSubTask(subTaskId: Int)
    suspend fun updateSubTask(subTask: SubTask)
}
