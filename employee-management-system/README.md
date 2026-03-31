# 🧑‍💼 Employee Management System

A full-stack web application built with **Spring Boot**, **Thymeleaf**, **MySQL**, and **REST APIs** — designed as a resume-ready Java project demonstrating industry-standard patterns.

---

## 🛠️ Tech Stack

| Layer       | Technology                     |
|-------------|-------------------------------|
| Backend     | Spring Boot 3.2, Java 17       |
| Frontend    | Thymeleaf, Bootstrap 5, HTML/CSS |
| Database    | MySQL 8+                       |
| ORM         | Spring Data JPA / Hibernate    |
| Validation  | Jakarta Bean Validation        |
| Build       | Maven                          |
| Testing     | JUnit 5, Mockito, MockMvc      |

---

## ✨ Features

- ✅ **Full CRUD** — Create, Read, Update, Delete employees
- ✅ **REST API** — Complete RESTful endpoints with JSON responses
- ✅ **MVC Views** — Server-side rendered Thymeleaf pages
- ✅ **Dashboard** — Stats: total, active, department breakdown
- ✅ **Search & Filter** — Search by name/email, filter by department
- ✅ **Pagination & Sorting** — Server-side pagination on all list views
- ✅ **Validation** — Server + client-side form validation
- ✅ **Exception Handling** — Global exception handler with meaningful errors
- ✅ **Status Management** — Active / Inactive / On Leave
- ✅ **Unit & Integration Tests** — JUnit 5 + Mockito + MockMvc

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8+

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/employee-management-system.git
cd employee-management-system
```

### 2. Configure the Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

### 4. Open in Browser

```
http://localhost:8080/employees/dashboard
```

---

## 📡 REST API Endpoints

Base URL: `http://localhost:8080/api/v1`

| Method   | Endpoint                        | Description              |
|----------|---------------------------------|--------------------------|
| `GET`    | `/employees`                    | Get all employees (paginated) |
| `GET`    | `/employees/{id}`               | Get employee by ID       |
| `GET`    | `/employees/search?query=...`   | Search employees         |
| `POST`   | `/employees`                    | Create new employee      |
| `PUT`    | `/employees/{id}`               | Update employee          |
| `PATCH`  | `/employees/{id}/status`        | Update status            |
| `DELETE` | `/employees/{id}`               | Delete employee          |
| `GET`    | `/employees/stats`              | Get dashboard statistics |
| `GET`    | `/employees/departments`        | Get all departments      |

### Example: Create Employee

```http
POST /api/v1/employees
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+1234567890",
  "department": "Engineering",
  "jobTitle": "Software Engineer",
  "salary": 75000,
  "hireDate": "2024-01-15",
  "status": "ACTIVE"
}
```

### Example Response

```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.com",
    "department": "Engineering",
    "jobTitle": "Software Engineer",
    "salary": 75000.0,
    "hireDate": "2024-01-15",
    "status": "ACTIVE"
  }
}
```

---

## 🗂️ Project Structure

```
src/
├── main/
│   ├── java/com/ems/
│   │   ├── EmployeeManagementSystemApplication.java
│   │   ├── controller/
│   │   │   ├── EmployeeController.java        ← MVC (Thymeleaf)
│   │   │   └── EmployeeRestController.java    ← REST API
│   │   ├── model/
│   │   │   └── Employee.java                  ← JPA Entity
│   │   ├── dto/
│   │   │   ├── EmployeeDTO.java
│   │   │   └── ApiResponse.java
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java        ← JPA Repository
│   │   ├── service/
│   │   │   ├── EmployeeService.java           ← Interface
│   │   │   └── EmployeeServiceImpl.java       ← Implementation
│   │   └── exception/
│   │       ├── EmployeeNotFoundException.java
│   │       ├── DuplicateEmailException.java
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── templates/
│       │   ├── fragments/layout.html
│       │   └── employee/
│       │       ├── dashboard.html
│       │       ├── list.html
│       │       ├── form.html
│       │       └── view.html
│       ├── static/css/style.css
│       └── application.properties
└── test/
    └── java/com/ems/
        ├── service/EmployeeServiceTest.java
        └── controller/EmployeeRestControllerTest.java
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 📌 Key Design Patterns Used

- **MVC Pattern** — Controller → Service → Repository
- **DTO Pattern** — Separation of API layer from entity layer
- **Repository Pattern** — Spring Data JPA abstractions
- **Service Layer** — Business logic decoupled from controllers
- **Global Exception Handling** — `@ControllerAdvice`
- **Builder Pattern** — Lombok `@Builder` on DTOs and entities

---

## 👨‍💻 Author

Built as a full-stack Java portfolio project.  
**Stack:** Java 17 · Spring Boot 3 · Thymeleaf · MySQL · REST API · JUnit 5
