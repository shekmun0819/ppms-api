package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.model.ReportAccess;
import com.example.ppms.repository.ReportAccessRepository;
import com.example.ppms.repository.ReportRepository;
import com.example.ppms.repository.UserRepository;
import com.example.ppms.service.ReportAccessService;

@SpringBootTest
public class ReportAccessImplTests {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportAccessRepository reportAccessRepository;

    @Autowired
    private ReportAccessService reportAccessService;

    @Test
    void TestSaveReportAccess() {
        // Create test input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);

        var allUsers = userRepository.findAll();
        var lastUser = allUsers.get(allUsers.size() - 1);

        // Get previous number of records
        int previousNumOfReportAccess = reportAccessRepository.findAll().size();

        // Run unit testing
        reportAccessService.saveReportAccess(lastReport.getId(), lastUser.getId());
        int currentNumOfReportAccess = reportAccessRepository.findAll().size();
        assertEquals(previousNumOfReportAccess + 1, currentNumOfReportAccess);
    }

    @Test
    void TestReportIdNotFoundAndSaveReportAccess() {
        // Create test input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);
        String lastReportId = lastReport.getId() + "123xyz";

        var allUsers = userRepository.findAll();
        var lastUser = allUsers.get(allUsers.size() - 1);

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportAccessService.saveReportAccess(lastReportId, lastUser.getId()));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    void TestUserIdNotFoundAndSaveReportAccess() {
        // Create test input
        var allReports = reportRepository.findAll();
        var lastReport = allReports.get(allReports.size() - 1);

        int userId = 999;

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportAccessService.saveReportAccess(lastReport.getId(), userId));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    void TestSetRequestAccepted() {
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.now();

        // Create test input
        ReportAccess reportAccess = new ReportAccess();
        reportAccess.setId(1);
        reportAccess.setStartDate(startDate);
        reportAccess.setEndDate(endDate);

        // Run unit testing
        reportAccessService.setRequestAccepted(reportAccess);
        var updatedReportAccess = reportAccessRepository.findById(reportAccess.getId());
        assertEquals(endDate, updatedReportAccess.get().getEndDate());
    }

    @Test
    void TestSetRequestAcceptedFail() {
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.now();

        // Create test input
        ReportAccess reportAccess = new ReportAccess();
        reportAccess.setId(999);
        reportAccess.setStartDate(startDate);
        reportAccess.setEndDate(endDate);

        // Run unit testing
        Exception exception = assertThrows(Exception.class,
                () -> reportAccessService.setRequestAccepted(reportAccess));
        assertEquals(ResourceNotFoundException.class, exception.getClass());
    }
}
