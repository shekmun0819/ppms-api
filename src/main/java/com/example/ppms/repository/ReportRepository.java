package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String>, JpaSpecificationExecutor<Report> {
    List<Report> findAllByTypeIdAndUserId(int typeId, int userId);

    List<Report> findAllByIsFinalTrue();

    List<Report> findAllByIsPublishedTrue();

    List<Report> findAllByIsFinalTrueAndUserId(int userId);

    Report findByUserIdAndTypeIdAndIsFinalTrue(int userId, int typeId);

    List<Report> findAllByIsFinalTrueAndUserId(int stuId, Sort sort);

    List<Report> findAllByTypeIdAndUserId(int typeId, int userId, Sort by);
}
