package com.example.ppms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.model.Company;
import com.example.ppms.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {
  
  @Autowired
	private CompanyRepository companyRepository;

  @Override
	public void saveCompany(Company company) {
		company.setCompanyName(company.getCompanyName().toUpperCase());
		company.setAddress(company.getAddress().toUpperCase());
		companyRepository.save(company);
	}

	public Optional<Company> getCompany(String id) {
		Integer companyId = Integer.parseInt(id);
		return companyRepository.findById(companyId);
	}

	public void updateCompany(Company company) {
		companyRepository.findById(company.getId()).ifPresent(company1 -> {
			company1.setCompanyName(company.getCompanyName());
			company1.setAddress(company.getAddress());
			company1.setContact(company.getContact());
			companyRepository.save(company1);
		});
	}

	@Override
	public List<Company> getAllCompanies() {
		return companyRepository.findAll(Sort.by(Sort.Direction.ASC, "companyName"));
	}

	public void importCompany(Company[] companies) {
		for (Company company : companies) {
			saveCompany(company);
		}
	}
}
