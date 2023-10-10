package com.example.ppms.service;

import java.util.List;

import com.example.ppms.model.ReportAccess;

public interface ReportAccessService {

	public List<ReportAccess> getAllReportAccess();

	public List<ReportAccess> getAllReportAccessByUserId(int userId);

	public ReportAccess getReportAccessById(int id);

	public ReportAccess saveReportAccess(String reportId, int userId);

	public ReportAccess setRequestRejected(int id);

	public ReportAccess setRequestAccepted(ReportAccess reportAccess);

}
