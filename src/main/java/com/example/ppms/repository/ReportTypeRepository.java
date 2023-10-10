package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.ReportType;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {

    List<ReportType> findAllByAcademicSessionId(int currentAcadId);
}
