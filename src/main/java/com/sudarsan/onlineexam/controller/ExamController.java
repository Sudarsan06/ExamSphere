package com.sudarsan.onlineexam.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sudarsan.onlineexam.entity.Course;
import com.sudarsan.onlineexam.entity.Exam;
import com.sudarsan.onlineexam.repository.CourseRepository;
import com.sudarsan.onlineexam.repository.ExamRepository;

@Controller
public class ExamController {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;

    public ExamController(ExamRepository examRepository,
                          CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/admin/exams")
    public String showExams(Model model) {
        List<Course> courses = courseRepository.findAll();
        List<Exam> exams = examRepository.findAll();

        model.addAttribute("courses", courses);
        model.addAttribute("exams", exams);

        return "manage-exams";
    }

    @PostMapping("/admin/exams")
    public String addExam(
            @RequestParam String course,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Integer durationMinutes,
            @RequestParam Integer totalMarks) {

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
}