# Student Management System

A REST API-based Student Management System developed using Java and Spring Boot. This project allows users to manage student records and store student information in a MySQL database.

## Technologies Used

- Java 25
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger UI
- Postman

## Features

- Add a new student
- View all students
- Get student details by ID
- Update student details
- Delete a student
- Search students by name
- Search students by department
- Pagination and sorting
- DTO-based request and response
- Input validation
- Global exception handling
- Duplicate email prevention
- API documentation using Swagger

## Database Setup

Create the database in MySQL:

```sql
CREATE DATABASE student_management;
```

## Application Configuration

Configure your MySQL username and password in `application.properties`:

```properties
spring.application.name=student-management-system
spring.datasource.url=jdbc:mysql://localhost:3306/student_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

Do not upload your actual database password to GitHub.

## How to Run the Project

1. Clone the repository.
2. Open the project in VS Code.
3. Configure the MySQL database credentials.
4. Run `EmployeesApplication.java`.
5. The application starts on port `8082`.

## Swagger API Documentation

Open this URL after starting the application:

http://localhost:8082/swagger-ui/index.html

You can view and test the REST APIs using Swagger UI.

## Running API tests

With the application running and connected to the configured MySQL database, run these commands from the project directory:

```powershell
node --test .\e2e\student-crud.test.mjs
node --test .\e2e\student-search.test.mjs
node --test .\e2e\student-dto-validation.test.mjs
```

The tests create uniquely named students and delete them during cleanup. Set `STUDENT_API_URL` to test a different application URL.

The existing Java test suite can be run with `.\mvnw.cmd test` (using the configured Java 25 JDK).

## GitHub Repository

https://github.com/vinod210-sys/student-management-system-java

## Developer

Vinod Kumar