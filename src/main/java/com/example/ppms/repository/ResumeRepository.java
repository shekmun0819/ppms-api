package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    public List<Resume> findAllByStudentId(Integer studentId);

}
