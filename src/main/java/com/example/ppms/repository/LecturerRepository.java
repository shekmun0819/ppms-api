package com.example.ppms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Lecturer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {

  Optional<Lecturer> findAllByUserId(Integer userId);
}
