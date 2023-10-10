package com.example.ppms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.ReportAccess;
import com.example.ppms.service.ReportAccessService;

@RestController
@RequestMapping("api/v1/report-access")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportAccessController {

	@Autowired
	private ReportAccessService reportAccessService;

	@GetMapping("/getAll")
	public List<ReportAccess> getAllReportAccess() {
		return reportAccessService.getAllReportAccess();
	}

	@GetMapping("/getAll/userId={id}")
	public List<ReportAccess> getAllReportAccessByUserId(@PathVariable("id") int userId) {
		return reportAccessService.getAllReportAccessByUserId(userId);
	}

	@GetMapping("{id}")
	public ResponseEntity<ReportAccess> getReportAccessById(@PathVariable("id") int id) {
		return new ResponseEntity<ReportAccess>(reportAccessService.getReportAccessById(id), HttpStatus.OK);
	}

	@PostMapping("/create")
	public ReportAccess saveReportAccess(@RequestParam("reportId") String reportId,
			@RequestParam("userId") int userId) {
		return reportAccessService.saveReportAccess(reportId, userId);
	}

	@PutMapping("/setReject/{id}")
	public ResponseEntity<ReportAccess> setReject(@PathVariable("id") int id) {
		return new ResponseEntity<ReportAccess>(reportAccessService.setRequestRejected(id), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<ReportAccess> setAccept(@RequestBody ReportAccess reportAccess) {
		return new ResponseEntity<ReportAccess>(reportAccessService.setRequestAccepted(reportAccess),
				HttpStatus.OK);
	}
}
