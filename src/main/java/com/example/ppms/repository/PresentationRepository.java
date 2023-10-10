package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Presentation;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Integer> {

  List<Presentation> findAllByCsmindId(Integer csmindId);
  List<Presentation> findAllByStudentIdInOrderByStudentNameAsc(List<Integer> ids);
}
