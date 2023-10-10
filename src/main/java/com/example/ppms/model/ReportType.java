package com.example.ppms.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
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
@Table(name = "ReportTypes")
public class ReportType extends BaseEntity {

	@Id
	@Column(name = "type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int milestone;
	private String type;
	private LocalDateTime dueDate;
	private boolean active;

	@OneToOne
	@JoinColumn(name = "academic_session_id", referencedColumnName = "academic_session_id", nullable = false)
	private AcademicSession academicSession;

	public ReportType() {
		super();
	}
}
