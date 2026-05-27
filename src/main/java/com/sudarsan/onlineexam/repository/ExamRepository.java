package com.sudarsan.onlineexam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sudarsan.onlineexam.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByCourse(String course);
}