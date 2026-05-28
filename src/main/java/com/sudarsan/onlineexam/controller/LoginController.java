package com.sudarsan.onlineexam.controller;

import java.util.List; 
import org.springframework.beans.factory.annotation.Value;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sudarsan.onlineexam.entity.Course;
import com.sudarsan.onlineexam.entity.Exam;
import com.sudarsan.onlineexam.entity.User;

import com.sudarsan.onlineexam.repository.CourseRepository;
import com.sudarsan.onlineexam.repository.ExamRepository;
import com.sudarsan.onlineexam.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	

    private final UserRepository userRepository;


    private final CourseRepository courseRepository;

    private final ExamRepository examRepository;

    private final PasswordEncoder passwordEncoder;
    
    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public LoginController(
            UserRepository userRepository,
            CourseRepository courseRepository,
            ExamRepository examRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;

        this.courseRepository = courseRepository;

        this.examRepository = examRepository;

        this.passwordEncoder = passwordEncoder;
    }

    // HOME PAGE

    @GetMapping("/")
    public String landingPage() {

        return "index";
    }

    // LOGIN PAGE

    @GetMapping("/login")
    public String showLoginPage() {

        return "login";
    }

    // LOGIN PROCESS

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Optional<User> optionalUser =
                userRepository.findByUsername(username);

        // USER NOT FOUND

        if (optionalUser.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Invalid username or password"
            );

            return "login";
        }

        User user = optionalUser.get();

        // PASSWORD CHECK

        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {

            model.addAttribute(
                    "error",
                    "Invalid username or password"
            );

            return "login";
        }

        // STORE USER IN SESSION

        session.setAttribute(
                "loggedInUser",
                user
        );

        // ADMIN LOGIN

        if ("ADMIN".equalsIgnoreCase(
                user.getRole()
        )) {

            return "redirect:/admin-dashboard";
        }

        // STUDENT LOGIN

        return "redirect:/student-dashboard";
    }

    // ADMIN DASHBOARD

    @GetMapping("/admin-dashboard")
    public String adminDashboard(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );

        // NO SESSION

        if (loggedInUser == null) {

            return "redirect:/login";
        }

        // ROLE CHECK

        if (!"ADMIN".equalsIgnoreCase(
                loggedInUser.getRole()
        )) {

            return "redirect:/login";
        }

        return "admin-dashboard";
    }

    // STUDENT DASHBOARD

    @GetMapping("/student-dashboard")
    public String studentDashboard(
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );

        if (loggedInUser == null) {

            return "redirect:/login";
        }

        List<Exam> exams =
                examRepository.findByCourse(
                        loggedInUser.getCourse()
                );

        model.addAttribute(
                "course",
                loggedInUser.getCourse()
        );

        model.addAttribute(
                "username",
                loggedInUser.getUsername()
        );

        model.addAttribute(
                "status",
                loggedInUser.getStatus()
        );

        model.addAttribute(
                "exams",
                exams
        );

        return "student-dashboard";
    }

    // REGISTER PAGE

    @GetMapping("/register")
    public String showRegisterPage(
            Model model) {

        List<Course> courses =
                courseRepository.findAll();

        model.addAttribute(
                "courses",
                courses
        );

        return "register";
    }

    // REGISTER PROCESS

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String course,
            Model model,
            HttpSession session) {

        // USERNAME EXISTS

        if (userRepository.findByUsername(
                username
        ).isPresent()) {

            model.addAttribute(
                    "error",
                    "Username already exists"
            );

            model.addAttribute(
                    "courses",
                    courseRepository.findAll()
            );

            return "register";
        }

        // EMAIL EXISTS

        if (userRepository.findByEmail(
                email
        ).isPresent()) {

            model.addAttribute(
                    "error",
                    "Email already exists"
            );

            model.addAttribute(
                    "courses",
                    courseRepository.findAll()
            );

            return "register";
        }

        // STORE TEMP DATA IN SESSION

        session.setAttribute(
                "pendingUsername",
                username
        );

        session.setAttribute(
                "pendingEmail",
                email
        );

        session.setAttribute(
                "pendingPassword",
                password
        );

        session.setAttribute(
                "pendingCourse",
                course
        );

        // GENERATE OTP

        String generatedOtp =
                String.format(
                        "%06d",
                        new Random().nextInt(
                                1000000
                        )
                );

        // STORE OTP IN SESSION

        session.setAttribute(
                "generatedOtp",
                generatedOtp
        );

        // OTP TIMESTAMP

        session.setAttribute(
                "otpTime",
                System.currentTimeMillis()
        );

        // SEND EMAIL

        sendOtpEmail(
                email,
                generatedOtp,
                username
        );

        model.addAttribute(
                "success",
                "OTP has been sent to your email."
        );

        return "verify-otp";
    }

    // VERIFY OTP PAGE

    @GetMapping("/verify-otp")
    public String showOtpPage() {

        return "verify-otp";
    }

    // VERIFY OTP PROCESS

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        String sessionOtp =
                (String) session.getAttribute(
                        "generatedOtp"
                );

        Long otpTime =
                (Long) session.getAttribute(
                        "otpTime"
                );

        // SESSION EXPIRED

        if (sessionOtp == null ||
            otpTime == null) {

            model.addAttribute(
                    "error",
                    "OTP session expired"
            );

            return "verify-otp";
        }

        // OTP EXPIRY (5 MINUTES)

        long currentTime =
                System.currentTimeMillis();

        if ((currentTime - otpTime)
                > 5 * 60 * 1000) {

            model.addAttribute(
                    "error",
                    "OTP expired. Please register again."
            );

            return "verify-otp";
        }

        // INVALID OTP

        if (!otp.equals(sessionOtp)) {

            model.addAttribute(
                    "error",
                    "Invalid OTP"
            );

            return "verify-otp";
        }

        // GET SESSION DATA

        String username =
                (String) session.getAttribute(
                        "pendingUsername"
                );

        String email =
                (String) session.getAttribute(
                        "pendingEmail"
                );

        String password =
                (String) session.getAttribute(
                        "pendingPassword"
                );

        String course =
                (String) session.getAttribute(
                        "pendingCourse"
                );

        // HASH PASSWORD

        String encodedPassword =
                passwordEncoder.encode(
                        password
                );

        // CREATE USER

        User newUser = new User(
                username,
                email,
                encodedPassword,
                "STUDENT",
                course
        );

        newUser.setStatus("ACTIVE");

        userRepository.save(newUser);

        // CLEAR SESSION DATA

        session.removeAttribute(
                "pendingUsername"
        );

        session.removeAttribute(
                "pendingEmail"
        );

        session.removeAttribute(
                "pendingPassword"
        );

        session.removeAttribute(
                "pendingCourse"
        );

        session.removeAttribute(
                "generatedOtp"
        );

        session.removeAttribute(
                "otpTime"
        );

        model.addAttribute(
                "success",
                "Registration successful. Please login."
        );

        return "login";
    }

    // RESEND OTP

    @PostMapping("/resend-otp")
    @ResponseBody
    public String resendOtp(
            HttpSession session) {

        String email =
                (String) session.getAttribute(
                        "pendingEmail"
                );

        String username =
                (String) session.getAttribute(
                        "pendingUsername"
                );

        if (email == null) {

            return "Session expired";
        }

        // GENERATE NEW OTP

        String otp =
                String.format(
                        "%06d",
                        new Random().nextInt(
                                1000000
                        )
                );

        session.setAttribute(
                "generatedOtp",
                otp
        );

        session.setAttribute(
                "otpTime",
                System.currentTimeMillis()
        );

        // SEND MAIL

        sendOtpEmail(
                email,
                otp,
                username
        );

        return "OTP resent successfully";
    }

    // LOGOUT

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

 // SEND OTP EMAIL

    private void sendOtpEmail(String email, String otp, String username) {
        try {
            String jsonBody = "{"
                + "\"sender\":{\"name\":\"ExamSphere\",\"email\":\"onlineexaminationportal1@gmail.com\"},"
                + "\"to\":[{\"email\":\"" + email + "\",\"name\":\"" + username + "\"}],"
                + "\"subject\":\"ExamSphere - OTP Verification\","
                + "\"textContent\":\"Dear " + username + ",\\n\\nYour OTP is: " + otp
                + "\\n\\nThis OTP is valid for 5 minutes.\\n\\nRegards,\\nExamSphere\""
                + "}";

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("Content-Type", "application/json")
                .header("api-key", brevoApiKey)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            java.net.http.HttpResponse<String> response = client
                .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                throw new RuntimeException("Brevo API error status=" + response.statusCode() + " body=" + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email details: " + e.getMessage(), e);
        }
    }
}

