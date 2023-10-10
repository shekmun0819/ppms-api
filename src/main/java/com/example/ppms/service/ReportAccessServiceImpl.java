package com.example.ppms.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.ppms.constants.ReportAccessConstant;
import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.model.Report;
import com.example.ppms.model.ReportAccess;
import com.example.ppms.model.User;
import com.example.ppms.repository.ReportAccessRepository;
import com.example.ppms.repository.ReportRepository;
import com.example.ppms.repository.UserRepository;

@Service
public class ReportAccessServiceImpl implements ReportAccessService {

	@Autowired
	private ReportAccessRepository reportAccessRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<ReportAccess> getAllReportAccess() {
		return reportAccessRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	@Override
	public List<ReportAccess> getAllReportAccessByUserId(int userId) {
		return reportAccessRepository.findAllByUserId(userId);
	}

	@Override
	public ReportAccess getReportAccessById(int id) {
		return reportAccessRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report request", "Id", id));
	}

	@Override
	public ReportAccess saveReportAccess(String reportId, int userId) {

		Report report = reportRepository.findById(reportId)
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", reportId));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		ReportAccess reportAccess = new ReportAccess();

		reportAccess.setReport(report);
		reportAccess.setUser(user);
		reportAccess.setRequestStatus(ReportAccessConstant.PENDING.toString());
		reportAccess.setStartDate(null);
		reportAccess.setEndDate(null);

		return reportAccessRepository.save(reportAccess);
	}

	@Override
	public ReportAccess setRequestRejected(int id) {
		ReportAccess existingReportAccess = reportAccessRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Report request", "Id", id));

		existingReportAccess.setRequestStatus(ReportAccessConstant.REJECTED.toString());
		reportAccessRepository.save(existingReportAccess);
		return existingReportAccess;
	}

	@Override
	public ReportAccess setRequestAccepted(ReportAccess reportAccess) {

		int id = reportAccess.getId();

		ReportAccess existingReportAccess = reportAccessRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Report request", "Id", id));

		existingReportAccess.setRequestStatus(ReportAccessConstant.ACCEPTED.toString());
		existingReportAccess.setStartDate(reportAccess.getStartDate());
		existingReportAccess.setEndDate(reportAccess.getEndDate());
		reportAccessRepository.save(existingReportAccess);
		return existingReportAccess;
	}

	@Scheduled(cron = "0 0 0 * * ?")
	// @Scheduled(fixedRate = 5000)
	public void checkReportAccessDate() throws ParseException {

		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Date today = simpleDateFormat.parse("2023-04-02");
		List<ReportAccess> accesses = reportAccessRepository.findAll();
		LocalDate today = LocalDate.now();

		for (ReportAccess access : accesses) {
			if (access.getEndDate() != null && access.getEndDate().isBefore(today)) {
				access.setRequestStatus(ReportAccessConstant.EXPIRED.toString());
				reportAccessRepository.save(access);
			}
		}
	}
}
