package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskApiTest {
    @Test
    fun `get tasks returns OK`() = testApplication {
        application { module() }
        val response = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `post creates a task`() = testApplication {
        application { module() }
        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Test task"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Test task"))
    }

    @Test
    fun `query parameter must be boolean`() = testApplication {
        application { module() }
        val response = client.get("/tasks?completed=sometimes")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `get missing task returns not found`() = testApplication {
        application { module() }
        val response = client.get("/tasks/999999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
