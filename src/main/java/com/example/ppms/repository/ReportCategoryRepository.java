package com.example.ppms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.ReportCategory;

@Repository
public interface ReportCategoryRepository extends JpaRepository<ReportCategory, Integer> {

}
