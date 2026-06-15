# 🎓 ExamSphere

ExamSphere is a web-based online examination platform designed to simplify the process of conducting, managing, and evaluating examinations digitally. 
The platform provides secure exam management, automated result processing, and performance tracking for students and administrators.

🌐 Live Demo: https://examsphere-production.up.railway.app/

---

## 🚀 Features

### Student Features
- Secure Login & Registration
- Browse Available Exams
- Attempt Online Tests
- View Scores and Results
- Track Examination History

### Admin Features
- Create and Manage Exams
- Add, Update, and Delete Questions
- Manage Students
- Monitor Exam Activity
- View Performance Reports

### Examination Features
- Timed Assessments
- Automatic Evaluation
- Real-Time Score Calculation
- Secure Examination Workflow
- Result Generation

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security

### Frontend
- HTML
- CSS
- JavaScript
- Thymeleaf 

### Database
- MySQL
- Railway Cloud

### Build Tool
- Maven

### Deployment
- Railway
- Render

---

## 📂 Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.examsphere
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── model
│   │       └── config
│   └── resources
│       ├── templates
│       ├── static
│       └── application.properties
└── test
```

---

## ⚙️ Setup Instructions

### Clone Repository

```bash
git clone <your-repository-url>
cd examsphere
```

### Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/examsphere
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run Application

Using Maven:

```bash
mvn spring-boot:run
```

Or:

```bash
mvn clean install
java -jar target/examsphere.jar
```

---

## 📊 Core Modules

- Authentication & Authorization
- Student Management
- Exam Management
- Question Management
- Result Processing
- Performance Analytics

---

## 🔒 Security Features

- Spring Security Authentication
- Role-Based Access Control
- Protected Routes
- Session Management
- Secure Password Storage

---

## 🎯 Future Enhancements

- AI-Based Proctoring
- Certificate Generation
- Email Notifications
- Question Bank Import/Export
- Multi-Language Support
- Advanced Analytics Dashboard

---

## 👨‍💻 Author

**Sudarsan Mahapatro**

- GitHub: https://github.com/Sudarsan06
- LinkedIn: www.linkedin.com/in/sudarsan-mahapatro

---


⭐ If you found this project useful, consider starring the repository.
