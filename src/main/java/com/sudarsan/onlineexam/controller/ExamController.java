package com.sudarsan.onlineexam.controller;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sudarsan.onlineexam.entity.Course;
import com.sudarsan.onlineexam.entity.Exam;
import com.sudarsan.onlineexam.entity.User;
import com.sudarsan.onlineexam.repository.CourseRepository;
import com.sudarsan.onlineexam.repository.ExamRepository;

@Controller
public class ExamController {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    
    private boolean isAdmin(HttpSession session) {

        User user =
            (User) session.getAttribute("loggedInUser");

        return user != null
                && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    public ExamController(ExamRepository examRepository,
                          CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/admin/exams")
    public String showExams(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Course> courses = courseRepository.findAll();
        List<Exam> exams = examRepository.findAll();

        model.addAttribute("courses", courses);
        model.addAttribute("exams", exams);

        return "manage-exams";
    }

    @PostMapping("/admin/exams")
    public String addExam(
            HttpSession session,
            @RequestParam String course,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Integer durationMinutes,
            @RequestParam Integer totalMarks) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Exam exam = new Exam(
                course,
                title,
                description,
                durationMinutes,
                totalMarks
        );

        examRepository.save(exam);

        return "redirect:/admin/exams";
    }
    
    @PostMapping("/admin/exams/{id}/delete")
    public String deleteExam(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        examRepository.deleteById(id);

        return "redirect:/admin/exams";
    }
}