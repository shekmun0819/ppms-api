package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.model.ReportType;
import com.example.ppms.repository.ReportTypeRepository;
import com.example.ppms.service.ReportTypeService;

@SpringBootTest
public class ReportTypeServiceImplTests {

    @Autowired
    private ReportTypeService reportTypeService;

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    @Test
    void TestSaveReportType() {
        // Create test input
        ReportType reportType = new ReportType();
        reportType.setMilestone(3);
        reportType.setType("FINAL");
        reportType.setDueDate(LocalDateTime.of(2023, Month.AUGUST, 6, 23, 59, 00));
        reportType.setActive(false);

        // Get previous number of records
        int previousNumOfReportType = reportTypeRepository.findAll().size();

        // Run unit testing
        reportTypeService.saveReportType(reportType);
        int currentNumOfReportType = reportTypeRepository.findAll().size();
        assertEquals(previousNumOfReportType + 1, currentNumOfReportType);
    }

    @Test
    void TestCannotSaveReportType() {
        // Create test input
        ReportType reportType = new ReportType();
        reportType.setMilestone(3);
        reportType.setType("FINAL");
        reportType.setDueDate(LocalDateTime.of(2023, Month.AUGUST, 6, 23, 59, 00));
        reportType.setActive(false);

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportTypeService.saveReportType(reportType));
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void TestUpdateReportType() {
        // Create test input
        ReportType reportType = new ReportType();
        reportType.setId(3);
        reportType.setMilestone(3);
        reportType.setType("FINAL");
        reportType.setDueDate(LocalDateTime.of(2023, Month.AUGUST, 6, 23, 59, 00));
        reportType.setActive(true);

        // Run unit testing
        reportTypeService.updateReportType(reportType);
        var updatedReportType = reportTypeRepository.findById(reportType.getId());
        assertEquals(reportType.isActive(), updatedReportType.get().isActive());
    }

    @Test
    void TestCannotUpdateReportType() {
        // Create test input
        ReportType reportType = new ReportType();
        reportType.setId(999);
        reportType.setMilestone(3);
        reportType.setType("FINAL");
        reportType.setDueDate(LocalDateTime.of(2023, Month.AUGUST, 6, 23, 59, 00));
        reportType.setActive(true);

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportTypeService.updateReportType(reportType));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

}
