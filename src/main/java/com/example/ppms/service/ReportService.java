package com.example.ppms.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.model.Report;

public interface ReportService {

    public List<Report> getAllReports();

    public List<Report> getAllReportsByType(int typeId, int userId);

    public List<Report> getAllFinalReports(int acadId);

    public List<Report> getAllPublishReports();

    public Report getReportById(String id);

    public Map<String, Object> getStudentReportById(String id);

    public Report saveReport(MultipartFile file, int typeId, int userId) throws Exception;

    public Boolean checkMultipleSubmission(int userId, int typeId);

    public Report setReportDetails(String id);

    public List<Report> publishReport(List<String> ids);

    public Report updatePublishStatus(Report report);

    public void deleteReport(String id);

    public String removeSubmission(String id);

    public List<Report> getAllFinalReportsByStudent(int stuId);

    public Map<String, Double> getTotalReportSubmission(int typeId);
}
