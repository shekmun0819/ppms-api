package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.repository.ReportRepository;
import com.example.ppms.service.ReportService;

@SpringBootTest
public class ReportServiceImplTests {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;

    @Test
    void TestDeleteReport() {
        // Create Test Input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);
        String reportId = lastReport.getId();

        // Run unit testing
        reportService.deleteReport(reportId);
        var retrievedReport = reportRepository.findById(reportId);
        assertTrue(retrievedReport.isEmpty());
    }

    @Test
    void TestCannotDeleteReport() {
        // Create Test Input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);
        String reportId = lastReport.getId() + "123xyz";

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportService.deleteReport(reportId));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    void TestSetReportDetails() {
        // Create test input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);
        if (lastReport.isFinal()) {
            lastReport.setFinal(false);
            reportRepository.save(lastReport);
        }
        String reportId = lastReport.getId();

        // Run unit testing
        reportService.setReportDetails(reportId);
        var updatedReport = reportRepository.findById(reportId);
        assertEquals(true, updatedReport.get().isFinal());
    }

    @Test
    void TestCannotSetReportDetails() {
        // Create test input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);
        if (lastReport.isFinal()) {
            lastReport.setFinal(false);
            reportRepository.save(lastReport);
        }
        String reportId = lastReport.getId() + "123xyz";

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportService.setReportDetails(reportId));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }
}
