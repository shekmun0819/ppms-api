package com.example.ppms.service;

import java.util.List;
import java.util.Optional;

import com.example.ppms.model.Company;

public interface CompanyService {
  
  public void saveCompany(Company company);
  public Optional<Company> getCompany(String id);
  public void updateCompany(Company company);
  public List<Company> getAllCompanies();
  public void importCompany(Company[] companies);
}
