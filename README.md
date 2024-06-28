
# TaskManager API

A comprehensive API for managing tasks, subtasks, and comments within a project. This API is built using Spring Boot and provides endpoints for creating, reading, updating, and deleting tasks, subtasks, and comments.

## Features

- **Task Management**: Create, update, delete, and fetch tasks.
- **Subtask Management**: Add, update, delete, and fetch subtasks for a specific task.
- **Comment Management**: Add, update, delete, and fetch comments for a specific task.

## Getting Started

### Prerequisites

Ensure you have the following installed:

- Java 17
- Maven
- MariaDB

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/aaddyy227/task-manager.git
   cd task-manager
   ```

2.1 **The program will initialize the database automatically, with the "schema.sql" from resources.**
2.2 **You can import the populated sql from resources in your database if you want**

3. **Configure the application:**

    - Update `src/main/resources/application.properties` with your database configuration:
      ```properties
      # JPA/Hibernate properties
      server.port=8080
            
      # JPA-Properties
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.properties.hibernate.format_sql=true
            
      # Datasource
      spring.datasource.url=jdbc:mariadb://localhost:3306?useSSL=false
      spring.datasource.username=root
      spring.datasource.password=YauI'N.zDIe7yZ82jM2%
      spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
            
      # SqlInit
      spring.sql.init.mode=always
      spring.sql.init.schema-locations=classpath:schema.sql
            
      #logging
      logging.level.org.hibernate.SQL=DEBUG
      logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
      ```

4. **Build and run the application:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the API:**
   Open your browser or API client (e.g., Postman) and navigate to `http://localhost:8080`.

## API Documentation

### Task Controller

#### Get Task by ID
```http
GET /tasks/{id}
```
**Response:**
```json
{
  "id": "string",
  "title": "string",
  "description": "string",
  "dueDate": "2024-06-28",
  "responsible": "string",
  "subTasks": [
    {
      "id": "string",
      "title": "string",
      "description": "string",
      "dueDate": "2024-06-28",
      "responsible": "string"
    }
  ],
  "comments": [
    {
      "id": "string",
      "content": "string",
      "createdDate": "2024-06-28T00:53:54.435Z",
      "updatedDate": "2024-06-28T00:53:54.435Z"
    }
  ]
}
```

#### Create Task
```http
POST /tasks/add
```
**Request Body:**
```json
{
  "title": "New Task",
  "description": "Task Description",
  "dueDate": "2024-06-28",
  "responsible": "John Doe"
}
```
**Response:**
```json
{
  "id": "string",
  "title": "New Task",
  "description": "Task Description",
  "dueDate": "2024-06-28",
  "responsible": "John Doe",
  "subTasks": [],
  "comments": []
}
```

#### Update Task
```http
PUT /tasks/{id}
```
**Request Body:**
```json
{
  "title": "Updated Task Title",
  "description": "Updated Task Description",
  "dueDate": "2024-06-28",
  "responsible": "John Doe"
}
```
**Response:**
```json
{
  "id": "string",
  "title": "Updated Task Title",
  "description": "Updated Task Description",
  "dueDate": "2024-06-28",
  "responsible": "John Doe",
  "subTasks": [],
  "comments": []
}
```

#### Delete Task
```http
DELETE /tasks/{id}
```
**Response:**
```json
"Deleted task with id: {id}"
```

### Subtask Controller

#### Get Subtask by ID
```http
GET /subtasks/{id}
```
**Response:**
```json
{
  "id": "string",
  "title": "string",
  "description": "string",
  "dueDate": "2024-06-28",
  "responsible": "string"
}
```

#### Create Subtask
```http
POST /tasks/{taskId}/add-subtask
```
**Request Body:**
```json
{
  "title": "New Subtask",
  "description": "Subtask Description",
  "dueDate": "2024-06-28",
  "responsible": "Jane Doe"
}
```
**Response:**
```json
{
  "id": "string",
  "title": "New Subtask",
  "description": "Subtask Description",
  "dueDate": "2024-06-28",
  "responsible": "Jane Doe",
  "parentTask": {
    "id": "string",
    "title": "Task Title",
    "description": "Task Description",
    "dueDate": "2024-06-28",
    "responsible": "John Doe"
  }
}
```

### Comment Controller

#### Get Comments by Task ID
```http
GET /comments/task/{taskId}
```
**Response:**
```json
[
  {
    "id": "string",
    "content": "string",
    "createdDate": "2024-06-28T00:53:54.456Z",
    "updatedDate": "2024-06-28T00:53:54.456Z"
  }
]
```

#### Add Comment to Task
```http
POST /comments/task/add/{taskId}
```
**Request Body:**
```json
{
  "content": "New Comment"
}
```
**Response:**
```json
"Comment added!"
```

#### Update Comment
```http
PUT /comments/{commentId}
```
**Request Body:**
```json
{
  "content": "Updated Comment"
}
```
**Response:**
```json
{
  "id": "string",
  "content": "Updated Comment",
  "createdDate": "2024-06-28T00:53:54.453Z",
  "updatedDate": "2024-06-28T00:53:54.453Z"
}
```

#### Delete Comment
```http
DELETE /comments/{commentId}
```
**Response:**
```json
"Deleted comment with id: {commentId}"
```

## Postman Collection

You can find the Postman collection for testing the API [here](https://pastebin.com/raw/B5VVb1g1).

---

Created by aaddyy227(https://github.com/aaddyy227)
