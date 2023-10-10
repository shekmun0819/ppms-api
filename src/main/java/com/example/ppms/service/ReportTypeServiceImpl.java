package com.example.ppms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.model.ReportType;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.ReportTypeRepository;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {

	@Autowired
	private ReportTypeRepository reportTypeRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Override
	public List<ReportType> getAllReportTypes() {
		return reportTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	@Override
	public List<ReportType> getAllReportTypesByAcademicSession() {
		int currentAcadId = 0;
		boolean currentAcadSessionExist = academicSessionRepository.findByActiveTrue().isPresent();

		if (currentAcadSessionExist) {
			currentAcadId = academicSessionRepository.findByActiveTrue().get().getId();
			return reportTypeRepository.findAllByAcademicSessionId(currentAcadId);
		} else {
			return null;
		}
	}

	@Override
	public ReportType saveReportType(ReportType reportType) {
		academicSessionRepository.findByActiveTrue().ifPresent(currentAcadSession -> {
			reportType.setAcademicSession(currentAcadSession);
		});

		if (reportType.getAcademicSession().equals(null)) {
			throw new ResourceNotFoundException();
		} else {
			return reportTypeRepository.save(reportType);
		}
	}

	@Override
	public ReportType getReportTypeById(int id) {
		return reportTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report Type", "Id", id));
	}

	@Override
	public ReportType updateReportType(ReportType reportType) {
		// Check if report type with given id exists in DB
		ReportType existingReportType = reportTypeRepository.findById(reportType.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Report type", "Id", reportType.getId()));
		existingReportType.setMilestone(reportType.getMilestone());
		existingReportType.setType(reportType.getType());
		existingReportType.setDueDate(reportType.getDueDate());
		existingReportType.setActive(reportType.isActive());
		reportTypeRepository.save(existingReportType);
		return existingReportType;
	}
}
