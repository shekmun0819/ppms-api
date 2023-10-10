package com.example.ppms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.exception.ResourceNotFoundException;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.Report;
import com.example.ppms.model.ReportType;
import com.example.ppms.model.Student;
import com.example.ppms.model.User;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.repository.ReportRepository;
import com.example.ppms.repository.ReportTypeRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;
import io.jsonwebtoken.io.IOException;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReportTypeRepository reportTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PracticumProjectRepository practicumProjectRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Override
	public List<Report> getAllReports() {
		return reportRepository.findAll();
	}

	@Override
	public List<Report> getAllReportsByType(int typeId, int userId) {
		return reportRepository.findAllByTypeIdAndUserId(typeId, userId, Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	@Override
	public List<Report> getAllFinalReports(int acadId) {
		List<Report> reports = reportRepository.findAllByIsFinalTrue();
		List<Report> filteredReports = new ArrayList<Report>();

		if (acadId > 0) {
			for (Report report : reports) {
				if (report.getProject().getAcademicSession().getId() == acadId) {
					filteredReports.add(report);
				}
			}
			return filteredReports;

		} else {
			return reports;
		}
	}

	@Override
	public List<Report> getAllPublishReports() {
		return reportRepository.findAllByIsPublishedTrue();
	}

	// public Map<String,Object> getAllPublishReports(userId) {
	// Map<String,Object> map = new HashMap<>();
	// List<Report> report = new ArrayList<Report>();

	// map.put("report", userRepository.findById(userId));
	// map.put("student", studentRepository.findAllByUserId(userId));

	// return map;
	// }

	@Override
	public Report getReportById(String id) {
		return reportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id));
	}

	@Override
	public Map<String, Object> getStudentReportById(String id) {
		Map<String, Object> map = new HashMap<>();

		Report report = reportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id));
		int userId = report.getUser().getId();

		map.put("report", report);
		map.put("student", studentRepository.findAllByUserId(userId));

		return map;
	}

	@Override
	public Report saveReport(MultipartFile file, int typeId, int userId) throws Exception {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		ReportType reportType = reportTypeRepository.findById(typeId).get();
		PracticumProject project = practicumProjectRepository.findByStudentId(userId);
		User user = userRepository.findById(userId).get();
		String newReportId = "";

		try {
			if (fileName.contains("..")) {
				throw new Exception("Filename contains invalid path sequence " + fileName);
			}

			Report report = new Report();

			report.setSimilarityScore(0);
			report.setFileName(fileName);
			report.setFileType(file.getContentType());
			report.setFinal(false);
			report.setPublished(false);
			report.setData(file.getBytes());
			report.setType(reportType);
			report.setProject(project);
			report.setUser(user);

			Report newReport = reportRepository.save(report);

			newReportId = newReport.getId();

			double simAvgScore = getSimilarityScore(newReportId, userId);

			newReport.setSimilarityScore(simAvgScore);

			return reportRepository.save(newReport);

		} catch (Exception e) {
			reportRepository.deleteById(newReportId);
			throw new Exception("Could not save file: " + fileName);
		}
	}

	@Override
	public Boolean checkMultipleSubmission(int userId, int typeId) {
		List<Report> reports = reportRepository.findAllByTypeIdAndUserId(typeId, userId);
		boolean isMultiple = false;

		for (Report report : reports) {
			if (report.isFinal()) {
				isMultiple = true;
				return isMultiple;
			}
		}
		return isMultiple;
	}

	@Override
	public Report setReportDetails(String id) {
		Report newReport = reportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id));

		newReport.setFinal(true);

		reportRepository.save(newReport);

		return newReport;
	}

	@Override
	public List<Report> publishReport(List<String> reportIds) {
		List<Report> reports = new ArrayList<Report>();

		reportIds.forEach(id -> {
			Report report = getReportById(id);
			report.setPublished(true);
			reports.add(report);
			reportRepository.save(report);
		});

		return reports;
	}

	@Override
	public Report updatePublishStatus(Report report) {
		Report newReport = reportRepository.findById(report.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", report.getId()));

		newReport.setPublished(report.isPublished());
		reportRepository.save(newReport);
		return newReport;
	}

	@Override
	public void deleteReport(String id) {

		reportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id));
		reportRepository.deleteById(id);
	}

	@Override
	public String removeSubmission(String id) {
		Report newReport = reportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id));
		newReport.setFinal(false);
		reportRepository.save(newReport);
		return "Submission removed successfully";
	}

	@Override
	public List<Report> getAllFinalReportsByStudent(int stuId) {
		return reportRepository.findAllByIsFinalTrueAndUserId(stuId, Sort.by(Sort.Direction.DESC, "updatedAt"));
	}

	@Override
	public Map<String, Double> getTotalReportSubmission(int typeId) {

		Map<String, Double> map = new HashMap<>();

		List<Student> cdsStudentList = new ArrayList<>();
		List<Student> cdtStudentList = new ArrayList<>();

		int currentAcadId = 0;
		int totalCDS590Student = 0;
		int totalCDT594Student = 0;
		int totalCDS590Submission = 0;
		int totalCDT594Submission = 0;
		double CDS590Percentage = 0;
		double CDT594Percentage = 0;
		double overallPercentage = 0;

		boolean currentAcadSessionExist = academicSessionRepository.findByActiveTrue().isPresent();

		if (currentAcadSessionExist) {
			currentAcadId = academicSessionRepository.findByActiveTrue().get().getId();
			cdsStudentList = studentRepository.findAllByCourseCodeAndAcademicSessionId("CDS590", currentAcadId);
			cdtStudentList = studentRepository.findAllByCourseCodeAndAcademicSessionId("CDT594", currentAcadId);

			totalCDS590Student = cdsStudentList.size();
			totalCDT594Student = cdtStudentList.size();
		}

		for (Student student : cdsStudentList) {
			int userId = student.getUser().getId();

			Report report = reportRepository.findByUserIdAndTypeIdAndIsFinalTrue(userId, typeId);

			if (report != null) {
				totalCDS590Submission++;
			}
		}

		for (Student student : cdtStudentList) {
			int userId = student.getUser().getId();
			Report report = reportRepository.findByUserIdAndTypeIdAndIsFinalTrue(userId, typeId);

			if (report != null) {
				totalCDT594Submission++;
			}
		}

		CDS590Percentage = 100 * totalCDS590Submission / totalCDS590Student;
		CDT594Percentage = 100 * totalCDT594Submission / totalCDT594Student;
		overallPercentage = (double) (totalCDS590Submission + totalCDT594Submission) /
				(totalCDS590Student + totalCDT594Student) * 100;

		map.put("CDS590", CDS590Percentage);
		map.put("CDT594", CDT594Percentage);
		map.put("Overall", overallPercentage);

		return map;
	}

	public double getSimilarityScore(String reportId, int userId) throws java.io.IOException, InterruptedException {

		double simAvgScore = 0;
		String apiUrl = "http://127.0.0.1:5000/similarity-check";

		// Set up HttpClient
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

			// Input data
			// String inputData = String.format("{\"input\": \"%s\"}", reportId);
			String inputData = String.format("{\"reportId\": \"%s\", \"userId\":\"%d\"}", reportId, userId);

			// Set the URL of the Python server
			HttpPost httpPost = new HttpPost(apiUrl);

			// Set the input data as the request body
			StringEntity requestEntity = new StringEntity(inputData, ContentType.APPLICATION_JSON);
			httpPost.setEntity(requestEntity);

			// Execute the request
			HttpResponse response = httpClient.execute(httpPost);

			// Read the response
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				String responseBody = EntityUtils.toString(responseEntity);
				System.out.println("Response from Python server: " + responseBody);
				simAvgScore = Double.parseDouble(responseBody);
			}
			return simAvgScore;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
