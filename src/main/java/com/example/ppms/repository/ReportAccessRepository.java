package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.ReportAccess;

@Repository
public interface ReportAccessRepository extends JpaRepository<ReportAccess, Integer> {
    List<ReportAccess> findAllByUserId(int userId);
}
