# Notes API

A simple RESTful API for note-taking application built with Spring Boot.

## Features

- Create, read, update, and delete notes
- Input validation
- Error handling
- In-memory data storage
- RESTful API design
- **Swagger/OpenAPI documentation**
- Interactive API testing interface

## Prerequisites

- Docker and Docker Compose

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd notes-api
```

### 2. Build and Run with Docker

```bash
# Build the Docker image
docker build -t notes-api .

# Run the container
docker run -p 8080:8080 notes-api
```

The API will be available at `http://localhost:8080`

## API Documentation

### Swagger UI
Once the application is running, you can access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

The Swagger UI provides an interactive interface where you can:
- View all available endpoints
- Test API calls directly from the browser
- View request/response schemas
- See example requests and responses


## Testing

### Run Tests Locally
```bash
mvn test
```

### Run Tests in Docker
```bash
# Build and run tests in Docker
docker build --target build -t notes-api-test .
docker run notes-api-test mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/notesapi/
│   │       ├── NotesApiApplication.java
│   │       ├── controller/
│   │       │   └── NoteController.java
│   │       ├── exception/
│   │       │   ├── NoteNotFoundException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── model/
│   │       │   └── Note.java
│   │       └── service/
│   │           └── NoteService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/notesapi/
            └── NotesApiApplicationTests.java
```

## Docker Commands

```bash
# Build the image
docker build -t notes-api .

# Run the container
docker run -p 8080:8080 notes-api

# Run in detached mode
docker run -d -p 8080:8080 --name notes-api-container notes-api

# Stop the container
docker stop notes-api-container

# Remove the container
docker rm notes-api-container

# View logs
docker logs notes-api-container

# Execute commands in running container
docker exec -it notes-api-container /bin/bash
```

## Technologies Used

- Spring Boot 3.2.0
- Spring Web
- Spring Validation
- SpringDoc OpenAPI 3 (Swagger)
- Maven
- Java 17
- Docker

## License

This project is for educational purposes.
