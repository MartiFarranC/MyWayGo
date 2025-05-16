package com.example.waygo.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val subTasks: List<SubTask> = emptyList()
)
