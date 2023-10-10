package com.example.ppms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.AcademicSession;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Integer> {
    Optional<AcademicSession> findByActiveTrue();

    Optional<AcademicSession> findByAcademicSessionAndSemester(String academicSession, int semester);

    List<AcademicSession> findAllByIdInOrderByCreatedAtDesc(List<Integer> ids);
}
