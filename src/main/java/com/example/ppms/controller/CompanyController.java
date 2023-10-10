package com.example.ppms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.Company;
import com.example.ppms.service.CompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/v1/company")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {
  
  @Autowired
	private CompanyService companyService;
	
  @PostMapping("/create")
	public String add(@RequestBody Company company) throws Exception {
		companyService.saveCompany(company);
		return "New company is added";
	}

	@GetMapping("/{id}")
	Optional<Company> getCompany(@PathVariable("id") String id) {
		return companyService.getCompany(id);
 	}

	 @PutMapping("/update")
	 public String update(@RequestBody Company company) {
		companyService.updateCompany(company);
		return "Company is updated";
	 }
  
	@GetMapping("/getAll")
	public List<Company> getAllCompanies() {
		return companyService.getAllCompanies();
	}

	@PostMapping("/import")
	public String importCompany(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		Company[] companies = objectMapper.readValue(json, Company[].class);
		companyService.importCompany(companies);
		return "List of companies is imported";
	}
}
