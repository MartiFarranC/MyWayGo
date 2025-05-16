package com.example.waygo.data.local.mapper

import com.example.waygo.data.local.entity.SubTaskEntity
import com.example.waygo.data.local.entity.TaskEntity
import com.example.waygo.domain.model.SubTask
import com.example.waygo.domain.model.Task

// De dominio a entidad
fun Task.toEntity(): TaskEntity =
    TaskEntity(id = id, title = title, description = description)

fun SubTask.toEntity(): SubTaskEntity =
    SubTaskEntity(id = id, parentTaskId = parentTaskId, title = title, description = description)

// De entidad a dominio
fun TaskEntity.toDomain(subTasks: List<SubTask>): Task =
    Task(id = id, title = title, description = description, subTasks = subTasks)

fun SubTaskEntity.toDomain(): SubTask =
    SubTask(id = id, parentTaskId = parentTaskId, title = title, description = description)