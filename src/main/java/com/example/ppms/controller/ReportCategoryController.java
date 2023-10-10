package com.example.ppms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.ReportCategory;
import com.example.ppms.service.ReportCategoryService;

@RestController
@RequestMapping("api/v1/report-category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportCategoryController {

	@Autowired
	private ReportCategoryService reportCategoryService;

	@GetMapping("/getAll")
	public List<ReportCategory> getAllReportCategories() {
		return reportCategoryService.getAllReportCategories();
	}
}
