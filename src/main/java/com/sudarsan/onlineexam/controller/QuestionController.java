package com.sudarsan.onlineexam.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sudarsan.onlineexam.entity.Exam;
import com.sudarsan.onlineexam.entity.Question;
import com.sudarsan.onlineexam.repository.ExamRepository;
import com.sudarsan.onlineexam.repository.QuestionRepository;

@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    public QuestionController(
            QuestionRepository questionRepository,
            ExamRepository examRepository) {

        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
    }

    // MANAGE QUESTIONS PAGE
    @GetMapping("/admin/exams/{examId}/questions")
    public String manageQuestions(
            @PathVariable Long examId,
            Model model) {

        Optional<Exam> optionalExam =
                examRepository.findById(examId);

        if (optionalExam.isEmpty()) {
            return "redirect:/admin/exams";
        }

        Exam exam = optionalExam.get();

        List<Question> questions =
                questionRepository.findByExamId(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("questions", questions);

        return "manage-questions";
    }

    // ADD QUESTION
    @PostMapping("/admin/exams/{examId}/questions")
    public String addQuestion(

            @PathVariable Long examId,

            @RequestParam String questionText,
            @RequestParam String optionA,
            @RequestParam String optionB,
            @RequestParam String optionC,
            @RequestParam String optionD,
            @RequestParam String correctAnswer,
            @RequestParam Integer marks) {

        Question question = new Question(
                examId,
                questionText,
                optionA,
                optionB,
                optionC,
                optionD,
                correctAnswer,
                marks
        );

        questionRepository.save(question);

        return "redirect:/admin/exams/" +
                examId +
                "/questions";
    }

    // DELETE QUESTION
    @GetMapping("/admin/questions/delete/{id}")
    public String deleteQuestion(
            @PathVariable Long id) {

        Optional<Question> optionalQuestion =
                questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            return "redirect:/admin/exams";
        }

        Question question = optionalQuestion.get();

        Long examId = question.getExamId();

        questionRepository.deleteById(id);

        return "redirect:/admin/exams/" +
                examId +
                "/questions";
    }

    // EDIT QUESTION PAGE
    @GetMapping("/admin/questions/edit/{id}")
    public String editQuestionPage(
            @PathVariable Long id,
            Model model) {

        Optional<Question> optionalQuestion =
                questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            return "redirect:/admin/exams";
        }

        model.addAttribute(
                "question",
                optionalQuestion.get()
        );

        return "edit-question";
    }

    // UPDATE QUESTION
    @PostMapping("/admin/questions/update")
    public String updateQuestion(

            @RequestParam Long id,

            @RequestParam String questionText,
            @RequestParam String optionA,
            @RequestParam String optionB,
            @RequestParam String optionC,
            @RequestParam String optionD,
            @RequestParam String correctAnswer,
            @RequestParam Integer marks) {

        Optional<Question> optionalQuestion =
                questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            return "redirect:/admin/exams";
        }

        Question question = optionalQuestion.get();

        question.setQuestionText(questionText);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setOptionD(optionD);
        question.setCorrectAnswer(correctAnswer);
        question.setMarks(marks);

        questionRepository.save(question);

        return "redirect:/admin/exams/" +
                question.getExamId() +
                "/questions";
    }
}