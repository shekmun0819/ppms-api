package com.example.ppms.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ProjectApplication")
@AllArgsConstructor
public class ProjectApplication extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "application_id")
	private int id;
	
	private String status;
	
	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
	private User student;
	
	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
	private PracticumProject practicumProject;

	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "resume_id", referencedColumnName = "resume_id")
	private Resume resume;

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public PracticumProject getPracticumProject() {
		return practicumProject;
	}

	public void setPracticumProject(PracticumProject practicumProject) {
		this.practicumProject = practicumProject;
	}

	public ProjectApplication() {
		super();
	}
}
