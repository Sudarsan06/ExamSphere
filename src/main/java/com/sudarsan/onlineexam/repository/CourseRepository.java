package com.sudarsan.onlineexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sudarsan.onlineexam.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}