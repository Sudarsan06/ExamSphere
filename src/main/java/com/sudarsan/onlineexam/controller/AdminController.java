package com.sudarsan.onlineexam.controller;

import java.util.List;
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

    // =========================
    // COURSE MANAGEMENT
    // =========================

    @GetMapping("/courses")
    public String showCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "manage-courses";
    }

    @PostMapping("/courses")
    public String addCourse(
            @RequestParam String courseName,
            @RequestParam String description) {

        Course course = new Course(courseName, description);
        courseRepository.save(course);

        return "redirect:/admin/courses";
    }

    // =========================
    // VIEW STUDENTS
    // =========================

    @GetMapping("/students")
    public String viewStudents(Model model) {
        List<User> students = userRepository.findByRole("STUDENT");
        model.addAttribute("students", students);
        return "view-students";
    }

    // =========================
    // REACTIVATE STUDENT ACCOUNT
    // =========================

    @PostMapping("/students/reactivate")
    public String reactivateStudent(@RequestParam Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus("ACTIVE");
            userRepository.save(user);
        }

        return "redirect:/admin/students";
    }
}