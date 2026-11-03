# Student Management System and Course Registration

A Java desktop application for managing student information, courses, and course registrations using Java Swing and MySQL.

## 1. Introduction

This project was developed to simplify academic management and course registration processes. It provides a graphical user interface for managing students, courses, registrations, and statistical reports.

The application uses Java Swing for the user interface, JDBC for database connectivity, and MySQL for data storage. It also applies Object-Oriented Programming (OOP), Java Collections Framework, file handling, and exception handling.

## 2. Main Features

- **Student Management:** Add, update, delete, search, and display student records.
- **Course Management:** Manage course information, credit hours, and instructors.
- **Course Registration:** Register and cancel courses, prevent duplicate registrations, and manage enrollment records.
- **Statistics and Reports:** Count students, courses, registrations, and calculate registered credit hours.
- **File Handling:** Import and export data using CSV or TXT files.
- **Input Validation:** Validate data and handle exceptions to improve system reliability.

## 3. Technologies Used

| Category | Technologies |
|---|---|
| Programming Language | Java |
| GUI Framework | Java Swing |
| Database | MySQL |
| Database Connectivity | JDBC |
| Programming Concepts | OOP, Java Collections |
| Additional Techniques | File Handling, Exception Handling |
| Development Tools | Visual Studio Code, MySQL Workbench |
| Version Control | Git & GitHub |

## 4. Database Design

The system consists of three main entities:

- **Student:** Stores student information.
- **Course:** Stores course information.
- **Enrollment:** Manages course registration records and relationships between students and courses.

Primary keys and foreign keys are used to maintain data integrity.

## 5. Installation and Setup

### 5.1. System Requirements

- JDK 17 or later.
- MySQL Server 8.0 or later.
- Visual Studio Code or Eclipse.
- MySQL Connector/J.

### 5.2. Clone the Repository

Open the terminal and execute:

```bash
git clone https://github.com/NguyenPhamThanhHa/StudentManager.git
cd StudentManager
```

### 5.3. Database Configuration

Create the database in MySQL:

```sql
CREATE DATABASE student_management;
```

Execute the SQL script provided in the project to create the required tables.

Configure the database connection in `DBConnection.java`:

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/student_management";

private static final String USER = "root";

private static final String PASSWORD = "your_password";
```

Replace `your_password` with your local MySQL password.

### 5.4. Install MySQL Connector/J

Download MySQL Connector/J from the official website:

https://dev.mysql.com/downloads/connector/j/

Extract the downloaded package and place the `.jar` file inside the project's `lib` folder.

### 5.5. Run the Application

Make sure MySQL Server is running and the database has been configured.

Open the project directory in Visual Studio Code and execute:

```powershell
.\run.bat
```

Alternatively, double-click `run.bat` to automatically compile and launch the application.

**Note:** Make sure JDK is installed, MySQL Connector/J is available in the `lib` folder, and the database connection settings are correct before running the application.

## 6. Screenshots

### Main Dashboard

<!-- Add main dashboard screenshot here -->

### Student Management

<!-- Add student management screenshot here -->

### Course Management

<!-- Add course management screenshot here -->

### Course Registration

<!-- Add course registration screenshot here -->

## 7. Future Improvements

- Implement role-based authentication.
- Improve user interface and user experience.
- Add Excel and PDF export functionality.
- Enhance search and reporting features.
- Improve database security and application performance.

## 8. Author

**Nguyen Pham Thanh Ha**
- **University:** Gia Dinh University
- **Major:** Software Engineering
- **GitHub:** [NguyenPhamThanhHa](https://github.com/NguyenPhamThanhHa)
