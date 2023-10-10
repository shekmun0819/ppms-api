package com.example.ppms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.ppms.ResponseData;
import com.example.ppms.model.Report;
import com.example.ppms.service.ReportService;

@RestController
@RequestMapping("api/v1/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/getAll")
	public List<Report> getAllReports() {
		return reportService.getAllReports();
	}

	@GetMapping("/getAll/{typeId}/{userId}")
	public ResponseEntity<List<Report>> getAllReportsByType(@PathVariable("typeId") int typeId,
			@PathVariable("userId") int userId) {
		return new ResponseEntity<List<Report>>(reportService.getAllReportsByType(typeId, userId), HttpStatus.OK);
	}

	@GetMapping("/getAll/finalReports/studentId={stuId}")
	public List<Report> getAllFinalReportsByStudent(@PathVariable("stuId") int stuId) {
		return reportService.getAllFinalReportsByStudent(stuId);
	}

	@GetMapping("/getAll/finalReports/academicSessionId={acadId}")
	public List<Report> getAllFinalReports(@PathVariable("acadId") int acadId) {
		return reportService.getAllFinalReports(acadId);
	}

	@GetMapping("/getAll/publishReports")
	public List<Report> getAllPublishReports() {
		return reportService.getAllPublishReports();
	}

	@GetMapping("{id}")
	public ResponseEntity<Report> getReportById(@PathVariable("id") String id) {
		return new ResponseEntity<Report>(reportService.getReportById(id), HttpStatus.OK);
	}

	@GetMapping("/student/{id}")
	public ResponseEntity<Map<String, Object>> getStudentReportById(@PathVariable("id") String id) {
		return new ResponseEntity<Map<String, Object>>(reportService.getStudentReportById(id), HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseData uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("typeId") int typeId, @RequestParam("userId") int userId) throws Exception {
		Report report = null;
		String downloadURL = "";

		report = reportService.saveReport(file, typeId, userId);
		downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/report/download/")
				.path(report.getId())
				.toUriString();

		return new ResponseData(report.getFileName(), downloadURL, file.getContentType(),
				file.getSize());
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
		Report report = null;
		report = reportService.getReportById(id);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(report.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + report.getFileName() + "\"")
				.body(new ByteArrayResource(report.getData()));
	}

	@GetMapping("/checkMultipleSubmission/userId={userId}&typeId={typeId}")
	public Boolean checkMultipleSubmission(@PathVariable("userId") int userId, @PathVariable("typeId") int typeId) {
		return reportService.checkMultipleSubmission(userId, typeId);
	}

	@PutMapping("/setFinal/{id}")
	public ResponseEntity<Report> setReportDetails(@PathVariable("id") String id) {
		return new ResponseEntity<Report>(reportService.setReportDetails(id), HttpStatus.OK);
	}

	@PutMapping("/setPublished/{ids}")
	public ResponseEntity<List<Report>> publishReport(@PathVariable("ids") List<String> ids) {
		return new ResponseEntity<List<Report>>(reportService.publishReport(ids), HttpStatus.OK);
	}

	@PutMapping("/updatePublishStatus")
	public ResponseEntity<Report> updatePublishStatus(@RequestBody Report report) {
		return new ResponseEntity<Report>(reportService.updatePublishStatus(report), HttpStatus.OK);
	}

	@PutMapping("/removeSubmission/{id}")
	public ResponseEntity<String> removeSubmission(@PathVariable("id") String id) {
		return new ResponseEntity<String>(reportService.removeSubmission(id), HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteReport(@PathVariable String id) {
		reportService.deleteReport(id);
		return new ResponseEntity<String>("Report deleted successfully.", HttpStatus.OK);
	}

	@GetMapping("/totalSubmission/{typeId}")
	public Map<String, Double> getTotalReportSubmission(@PathVariable("typeId") int typeId) {
		return reportService.getTotalReportSubmission(typeId);
	}
}
