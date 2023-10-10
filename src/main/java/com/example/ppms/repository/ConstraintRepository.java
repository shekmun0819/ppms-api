package com.example.ppms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Constraint;

@Repository
public interface ConstraintRepository extends JpaRepository<Constraint, Integer> {

  List<Constraint> findAllByCsmindId(Integer csmindId);
  List<Constraint> findAllByUserIdOrderByCsmindStartDateDesc(Integer userId);
  Optional<Constraint> findByCsmindIdAndUserId(Integer csmindId, Integer userId);
}
