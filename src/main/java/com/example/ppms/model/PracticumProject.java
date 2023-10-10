package com.example.ppms.model;

import org.springframework.web.multipart.MultipartFile;
import javax.persistence.Lob;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Table(name = "PracticumProjects")
public class PracticumProject extends BaseEntity {

	@Id
	@Column(name = "project_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String name;

	private String categories;
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	private String status;
	@Column(columnDefinition = "LONGTEXT")
	private String adminDescription;
	private String imageName;
	private String ndaName;
	private boolean isNDA;
	private String courseName;
	private String courseCode;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] imageData;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] ndaData;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "academic_session_id", referencedColumnName = "academic_session_id", nullable = false)
	private AcademicSession academicSession;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id", referencedColumnName = "user_id" /* , nullable = false */)
	private User student;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "supervisor_id", referencedColumnName = "user_id")
	private User supervisor;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "examiner_id", referencedColumnName = "user_id")
	private User examiner;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "panel_id", referencedColumnName = "user_id")
	private User panel;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "chair_id", referencedColumnName = "user_id")
	private User chair;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "host_id", referencedColumnName = "user_id" /* , nullable = false */)
	private User host;

	public PracticumProject() {
		super();
	}
}