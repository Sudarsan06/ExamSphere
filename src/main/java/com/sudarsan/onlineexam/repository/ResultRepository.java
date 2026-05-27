package com.sudarsan.onlineexam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sudarsan.onlineexam.entity.Result;

public interface ResultRepository
        extends JpaRepository<Result, Long> {

    List<Result> findByUsername(String username);

    Optional<Result> findByUsernameAndExamId(
            String username,
            Long examId
    );
}