package com.sudarsan.onlineexam.controller;

import java.util.List; 
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sudarsan.onlineexam.entity.Course;
import com.sudarsan.onlineexam.entity.User;
import com.sudarsan.onlineexam.repository.CourseRepository;
import com.sudarsan.onlineexam.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public AdminController(CourseRepository courseRepository,
                           UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    private boolean isAdmin(HttpSession session) {

        User user =
            (User) session.getAttribute("loggedInUser");

        return user != null
                && "ADMIN".equalsIgnoreCase(user.getRole());
    }
    
    // =========================
    // COURSE MANAGEMENT
    // =========================

    @GetMapping("/courses")
    public String showCourses(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);

        return "manage-courses";
    }
    
    @PostMapping("/courses")
    public String addCourse(
            HttpSession session,
            @RequestParam String courseName,
            @RequestParam String description) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Course course =
            new Course(courseName, description);

        courseRepository.save(course);

        return "redirect:/admin/courses";
    }

    // =========================
    // VIEW STUDENTS
    // =========================

    @GetMapping("/students")
    public String viewStudents(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<User> students =
            userRepository.findByRole("STUDENT");

        model.addAttribute("students", students);

        return "view-students";
    }
    // =========================
    // REACTIVATE STUDENT ACCOUNT
    // =========================

    @PostMapping("/students/reactivate")
    public String reactivateStudent(
            HttpSession session,
            @RequestParam Long id) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<User> optionalUser =
            userRepository.findById(id);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            user.setStatus("ACTIVE");

            userRepository.save(user);
        }

        return "redirect:/admin/students";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("courseCount", courseRepository.count());

        model.addAttribute(
                "studentCount",
                userRepository.findByRole("STUDENT").size()
        );

        return "admin-dashboard";
    }
    
}