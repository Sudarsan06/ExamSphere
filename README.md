# 🎓 ExamSphere

A full-stack online examination platform built using Spring Boot, SQL Server, Thymeleaf, HTML, CSS, and JavaScript.

ExamSphere enables educational institutions and training organizations to conduct online examinations through a secure, role-based platform with dedicated dashboards for administrators and students.

🌐 Live Demo: https://examsphere-production.up.railway.app/
---

## 🚀 Features

### 👨‍💼 Admin Module

* Secure Admin Login
* Create and Manage Exams
* Add, Edit and Delete Questions
* View Student Information
* Manage Exam Records
* Monitor Exam Activity
* Delete Exams and Questions

### 👨‍🎓 Student Module

* User Registration
* Email OTP Verification
* Secure Login
* View Available Exams
* Attempt Exams
* Automatic Score Calculation
* Exam History Tracking
* Responsive Dashboard

### 🔒 Security Features

* BCrypt Password Hashing
* OTP Email Verification
* Session-Based Authentication
* Role-Based Access Control
* Protected Admin Workflows

---

## 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate

### Frontend

* HTML5
* CSS3
* JavaScript
* Thymeleaf

### Database

* SQL Server

### Tools

* Git
* GitHub
* Maven

---

## 📂 Project Structure

```text
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── config
 └── templates
```

---

## ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/ExamSphere.git
```

### Navigate to Project

```bash
cd ExamSphere
```

### Configure Database

Update:

```properties
application.properties
```

```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=OnlineExamDB
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Run Application

```bash
mvn spring-boot:run
```

Application starts at:

```text
http://localhost:8080
```

---

## 📧 Email Configuration

ExamSphere uses Gmail SMTP for OTP verification.

Configure:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

---

## 📸 Screenshots

### Landing Page

<img width="1920" height="1080" alt="Screenshot (277)" src="https://github.com/user-attachments/assets/149db3bd-3bbf-42ae-bbd1-6b606fb26e2f" />

### Admin Dashboard

<img width="1920" height="1080" alt="Screenshot (281)" src="https://github.com/user-attachments/assets/2453ef56-cd65-47e3-a896-2420704b3cbb" />

### Manage Courses

<img width="1920" height="1080" alt="Screenshot (282)" src="https://github.com/user-attachments/assets/e4d8b7d4-0f67-4aa7-9883-18c01cb1fa77" />

### Student Dashboard

<img width="1920" height="1080" alt="Screenshot (279)" src="https://github.com/user-attachments/assets/eaba7138-ac80-42bc-b4a4-767c0b7f48d9" />

### Exam Interface

<img width="1920" height="1080" alt="Screenshot (280)" src="https://github.com/user-attachments/assets/e2e57d69-c1f7-41b5-a9f4-153b300f2a2e" />

---

## 🎯 Key Learning Outcomes

* Spring Boot MVC Architecture
* Authentication & Authorization
* Session Management
* OTP-Based Verification
* BCrypt Password Hashing
* SQL Database Integration
* Full-Stack Web Development
* Responsive UI Design

---

## 🔮 Future Enhancements

* Spring Security Integration
* JWT Authentication
* Exam Analytics Dashboard
* AI-Based Proctoring
* Leaderboards
* Certificate Generation
* Docker Deployment
* Cloud Hosting

---

## 👨‍💻 Author

**Sudarsan Mahapatro**

GitHub:
https://github.com/Sudarsan06

LinkedIn:
https://linkedin.com/in/sudarsan-mahapatro
