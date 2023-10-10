package com.example.ppms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {
  
  @Id
  @Column(name = "company_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

  @Column(unique=true)
  private String companyName;
  
  private String address;
	private String contact;

  public Company() {
    super();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public Company(String companyName, String address, String contact) {
    this.companyName = companyName;
    this.address = address;
    this.contact = contact;
  }

}
