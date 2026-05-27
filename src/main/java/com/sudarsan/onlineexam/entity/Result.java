package com.sudarsan.onlineexam.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long examId;

    private String examTitle;

    private Integer score;

    private Integer totalMarks;

    private LocalDateTime submittedAt;

    public Result() {
    }

    public Result(String username,
                  Long examId,
                  String examTitle,
                  Integer score,
                  Integer totalMarks,
                  LocalDateTime submittedAt) {

        this.username = username;
        this.examId = examId;
        this.examTitle = examTitle;
        this.score = score;
        this.totalMarks = totalMarks;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Long getExamId() {
        return examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}