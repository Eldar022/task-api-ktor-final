package com.example

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun io.ktor.server.application.Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Task API is running")
        }

        // GET /tasks?completed=true or GET /tasks?completed=false
        get("/tasks") {
            val completedParam = call.request.queryParameters["completed"]
            val completed = completedParam?.toBooleanStrictOrNull()

            if (completedParam != null && completed == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiError("Query parameter 'completed' must be true or false")
                )
                return@get
            }

            call.respond(TaskStore.all(completed))
        }

        // GET /tasks/{id}
        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null || id <= 0) {
                call.respond(HttpStatusCode.BadRequest, ApiError("Task id must be a positive integer"))
                return@get
            }

            val task = TaskStore.find(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound, ApiError("Task with id $id was not found"))
            } else {
                call.respond(task)
            }
        }

        // POST /tasks with JSON body: {"title":"Buy milk"}
        post("/tasks") {
            val request = try {
                call.receive<CreateTaskRequest>()
            } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiError("Request body must be valid JSON with a title"))
                return@post
            }

            if (request.title.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, ApiError("Title must not be blank"))
                return@post
            }

            val created = TaskStore.create(request.title)
            call.respond(HttpStatusCode.Created, created)
        }

        // DELETE /tasks/{id}
        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null || id <= 0) {
                call.respond(HttpStatusCode.BadRequest, ApiError("Task id must be a positive integer"))
                return@delete
            }

            if (TaskStore.delete(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ApiError("Task with id $id was not found"))
            }
        }
    }
}
