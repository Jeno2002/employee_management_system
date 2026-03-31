# 🧑‍💼 Employee Management System

## 📌 Project Description

The **Employee Management System** is a web-based application developed using **Spring Boot** that helps manage employee records efficiently. It allows users to perform CRUD operations such as adding, updating, viewing, and deleting employee details.

---

## 🚀 Technologies Used

* ☕ Java
* 🌱 Spring Boot
* 🗄️ MySQL
* 🎨 Thymeleaf
* 📦 Maven
* 🌐 HTML, CSS

---

## ✨ Features

* ➕ Add new employees
* 📋 View employee list
* ✏️ Update employee details
* ❌ Delete employees
* 🔍 View individual employee details
* ⚠️ Exception handling (custom errors)
* 🌐 REST API support

---

## 🗂️ Project Structure

```
src/main/java/com/ems/
│
├── controller        # Handles web & REST requests
├── service           # Business logic
├── repository        # Database interaction
├── model             # Entity classes
├── dto               # Data Transfer Objects
├── exception         # Custom exceptions
```

---

## ⚙️ How to Run the Project

### 1️⃣ Clone the repository

```
https://github.com/Jeno2002/employee-management-system.git
```

### 2️⃣ Open in IDE

* Open in **Eclipse / IntelliJ / VS Code**

### 3️⃣ Configure Database

Edit `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 4️⃣ Run the application

Run:

```
EmployeeManagementSystemApplication.java
```

### 5️⃣ Open in Browser

```
http://localhost:8080
```

---

## 📸 Screenshots

(Add your screenshots here)

* 🏠 Dashboard
* 📋 Employee List
* ➕ Add Employee Form

---

## 📌 API Endpoints (Optional)

* GET `/api/employees`
* POST `/api/employees`
* PUT `/api/employees/{id}`
* DELETE `/api/employees/{id}`

---

## 👨‍💻 Author

**Infant Jeno J**

---

## ⭐ Future Improvements

* 🔐 Authentication (Login/Register)
* 📊 Analytics Dashboard
* 📱 Responsive UI

---

## 📢 Note

This project is developed for learning and demonstration purposes.
