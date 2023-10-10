package com.example.ppms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;

import com.example.ppms.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

  Optional<Student> findAllByUserId(Integer userId);

  Student findByUserId(int userId);

  int countByCourseCodeAndAcademicSessionId(String courseCode, Integer currentAcadId);

  List<Student> findAllByCourseCodeAndAcademicSessionId(String courseCode, Integer currentAcadId);
  
  List<Student> findAllByAcademicSession_active(Boolean active, Sort sort);
}