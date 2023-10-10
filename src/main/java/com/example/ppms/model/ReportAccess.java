package com.example.ppms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ReportAccess")
public class ReportAccess extends BaseEntity {

	@Id
	@Column(name = "access_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String requestStatus;
	private LocalDate startDate;
	private LocalDate endDate;

	@OneToOne
	@JoinColumn(name = "report_id", referencedColumnName = "report_id", nullable = false)
	private Report report;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	public ReportAccess() {
		super();
	}
}
