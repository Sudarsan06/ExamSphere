package com.sudarsan.onlineexam.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sudarsan.onlineexam.entity.Exam;
import com.sudarsan.onlineexam.entity.Question;
import com.sudarsan.onlineexam.entity.Result;
import com.sudarsan.onlineexam.entity.User;
import com.sudarsan.onlineexam.repository.ExamRepository;
import com.sudarsan.onlineexam.repository.QuestionRepository;
import com.sudarsan.onlineexam.repository.ResultRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentExamController {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    public StudentExamController(
            ExamRepository examRepository,
            QuestionRepository questionRepository,
            ResultRepository resultRepository) {

        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    // OPEN EXAM
    @GetMapping("/student/exam/{examId}")
    public String takeExam(
            @PathVariable Long examId,
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Optional<Result> existingResult =
                resultRepository.findByUsernameAndExamId(
                        loggedInUser.getUsername(),
                        examId
                );

        // Prevent multiple attempts
        if (existingResult.isPresent()) {

            model.addAttribute(
                    "message",
                    "You have already attempted this exam."
            );

            return "exam-attempt-blocked";
        }

        Optional<Exam> optionalExam =
                examRepository.findById(examId);

        if (optionalExam.isEmpty()) {
            return "redirect:/login";
        }

        Exam exam = optionalExam.get();

        List<Question> questions =
                questionRepository.findByExamId(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("questions", questions);

        return "take-exam";
    }

    // SUBMIT EXAM
    @PostMapping("/student/exam/{examId}/submit")
    public String submitExam(
            @PathVariable Long examId,
            @RequestParam Map<String, String> answers,
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Optional<Exam> optionalExam =
                examRepository.findById(examId);

        if (optionalExam.isEmpty()) {
            return "redirect:/login";
        }

        Exam exam = optionalExam.get();

        List<Question> questions =
                questionRepository.findByExamId(examId);

        int score = 0;
        int actualTotalMarks = 0;

        // SCORE CALCULATION
        for (Question question : questions) {

            actualTotalMarks += question.getMarks();

            String selectedAnswer =
                    answers.get("q" + question.getId());

            if (selectedAnswer != null &&
                    selectedAnswer.equals(
                            question.getCorrectAnswer())) {

                score += question.getMarks();
            }
        }

        // SAVE RESULT
        Result result = new Result(
                loggedInUser.getUsername(),
                examId,
                exam.getTitle(),
                score,
                actualTotalMarks,
                LocalDateTime.now()
        );

        resultRepository.save(result);

        model.addAttribute("score", score);
        model.addAttribute("totalMarks", actualTotalMarks);

        return "result-page";
    }

    // VIEW STUDENT RESULTS
    @GetMapping("/student/results")
    public String studentResults(
            HttpSession session,
            Model model) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Result> results =
                resultRepository.findByUsername(
                        loggedInUser.getUsername()
                );

        model.addAttribute("results", results);

        return "student-results";
    }
}