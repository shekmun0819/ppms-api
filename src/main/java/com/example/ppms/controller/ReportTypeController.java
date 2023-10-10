package com.example.ppms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.ReportType;
import com.example.ppms.service.ReportTypeService;

@RestController
@RequestMapping("api/v1/report-type")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportTypeController {

	@Autowired
	private ReportTypeService reportTypeService;

	@GetMapping("/getAll")
	public List<ReportType> getAllReportTypes() {
		return reportTypeService.getAllReportTypes();
	}

	@GetMapping("/getAll/currentAcademicSession")
	public List<ReportType> getAllReportTypesByAcademicSession() {
		return reportTypeService.getAllReportTypesByAcademicSession();
	}

	@PostMapping("/create")
	public String createReportType(@RequestBody ReportType reportType) {
		reportTypeService.saveReportType(reportType);
		return "New report type is created";
	}

	@GetMapping("{id}")
	public ResponseEntity<ReportType> getReportTypeById(@PathVariable("id") int id) {
		return new ResponseEntity<ReportType>(reportTypeService.getReportTypeById(id), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<ReportType> updateReportType(@RequestBody ReportType reportType) {
		return new ResponseEntity<ReportType>(reportTypeService.updateReportType(reportType), HttpStatus.OK);
	}
}
