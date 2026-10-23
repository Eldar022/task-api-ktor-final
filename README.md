# Task API (Kotlin + Ktor)

A small REST API for managing tasks. It demonstrates Ktor routing, JSON serialization with `ContentNegotiation` and `kotlinx.serialization`, query/path parameters, and meaningful HTTP status codes.

## Requirements

- JDK 17
- Internet connection for Gradle to download dependencies

## Run in IntelliJ IDEA

1. Extract the ZIP archive.
2. In IntelliJ IDEA, choose **Open** and select the extracted `task-api-ktor` folder.
3. Allow IntelliJ to load/sync the Gradle project.
4. Run `Application.kt`, or use the terminal command `gradle run` if Gradle is installed.

The server listens on `http://localhost:8080`.

## Endpoints

| Method | Endpoint | Description | Success response |
|---|---|---|---|
| GET | `/` | Health message | `200 OK` |
| GET | `/tasks` | List all tasks | `200 OK` |
| GET | `/tasks?completed=true` | Filter tasks by completion | `200 OK` |
| GET | `/tasks?completed=false` | Filter incomplete tasks | `200 OK` |
| GET | `/tasks/{id}` | Get one task by ID | `200 OK`, or `404 Not Found` |
| POST | `/tasks` | Create a task | `201 Created` |
| DELETE | `/tasks/{id}` | Delete a task | `204 No Content`, or `404 Not Found` |

Invalid IDs and invalid `completed` query values return `400 Bad Request`. Error responses are JSON.

### Create a task

Send `POST http://localhost:8080/tasks` with `Content-Type: application/json` and body:

```json
{
  "title": "Buy milk"
}
```

Example PowerShell:

```powershell
$body = @{ title = "Buy milk" } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/tasks" -Method Post -ContentType "application/json" -Body $body
```

### Filter tasks

Open `http://localhost:8080/tasks?completed=true` in a browser, or use `false` to list incomplete tasks.

## Run tests

In the project terminal:

```powershell
gradle test
```

If Gradle is not installed globally, open the project in IntelliJ IDEA and run the tests from the Gradle tool window.

## Notes

Tasks are stored in memory, so they reset when the server restarts. This project is intended as a learning assignment, not a production database-backed service.
