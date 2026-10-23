package com.example

object TaskStore {
    private val tasks = linkedMapOf<Int, Task>()
    private var nextId = 1

    @Synchronized
    fun all(completed: Boolean? = null): List<Task> =
        tasks.values.filter { completed == null || it.completed == completed }

    @Synchronized
    fun find(id: Int): Task? = tasks[id]

    @Synchronized
    fun create(title: String): Task {
        val task = Task(id = nextId++, title = title.trim())
        tasks[task.id] = task
        return task
    }

    @Synchronized
    fun delete(id: Int): Boolean = tasks.remove(id) != null
}
