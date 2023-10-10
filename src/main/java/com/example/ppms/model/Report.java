package com.example.ppms.model;

import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "Reports")
public class Report extends BaseEntity {

	@Id
	@Column(name = "report_id")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private double similarityScore;
	private String fileName;
	private String fileType;
	private boolean isFinal;
	private boolean isPublished;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] data;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "type_id", referencedColumnName = "type_id", nullable = false)
	private ReportType type;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
	private PracticumProject project;

	public Report() {
		super();
	}

	public Report(double similarityScore, String fileName, String fileType,
			boolean isFinal, boolean isPublished, byte[] data, ReportType type,
			PracticumProject project, User user) {
		this.similarityScore = similarityScore;
		this.fileName = fileName;
		this.fileType = fileType;
		this.isFinal = isFinal;
		this.isPublished = isPublished;
		this.data = data;
		this.type = type;
		this.project = project;
		this.user = user;
	}

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "category_id", referencedColumnName = "category_id",
	// nullable = false)
	// private ReportCategory category;

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "academic_session_id", referencedColumnName =
	// "academic_session_id", nullable =
	// false)
	// private AcademicSession academicSession;
}
