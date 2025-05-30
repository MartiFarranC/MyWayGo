package com.example.waygo.data.repository

import com.example.waygo.data.local.dao.SubTaskDao
import com.example.waygo.data.local.dao.TaskDao
import com.example.waygo.data.local.mapper.toEntity
import com.example.waygo.data.local.mapper.toDomain
import com.example.waygo.domain.model.SubTask
import com.example.waygo.domain.model.Task
import com.example.waygo.domain.repository.TaskRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao
) : TaskRepository {

    override suspend fun getTasks(): List<Task> {
        val taskEntities = taskDao.getTasks()
        return taskEntities.map { taskEntity ->
            val subs = subTaskDao.getSubTasksForTask(taskEntity.id).map { it.toDomain() }
            taskEntity.toDomain(subs)
        }
    }

    override suspend  fun addTask(task: Task) {
        taskDao.addTask(task.toEntity())
    }

    override suspend  fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    override suspend  fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend  fun getSubTasksForTask(taskId: Int): List<SubTask> {
        return subTaskDao.getSubTasksForTask(taskId).map { it.toDomain() }
    }

    override suspend fun addSubTask(subTask: SubTask) {
        subTaskDao.addSubTask(subTask.toEntity())
    }

    override suspend fun deleteSubTask(subTaskId: Int) {
        subTaskDao.deleteSubTask(subTaskId)
    }

    override suspend fun updateSubTask(subTask: SubTask) {
        subTaskDao.updateSubTask(subTask.toEntity())
    }
}