package com.example.ppms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ppms.model.ReportCategory;
import com.example.ppms.repository.ReportCategoryRepository;

@Service
public class ReportCategoryServiceImpl implements ReportCategoryService {

	@Autowired
	private ReportCategoryRepository reportCategoryRepository;

	@Override
	public List<ReportCategory> getAllReportCategories() {
		return reportCategoryRepository.findAll();
	}
	
}
