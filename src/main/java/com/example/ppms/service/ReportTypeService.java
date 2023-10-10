package com.example.ppms.service;

import java.util.List;

import com.example.ppms.model.ReportType;

public interface ReportTypeService {

	public List<ReportType> getAllReportTypes();

	public List<ReportType> getAllReportTypesByAcademicSession();

	public ReportType saveReportType(ReportType reportType);

	public ReportType getReportTypeById(int id);

	public ReportType updateReportType(ReportType reportType);
}
