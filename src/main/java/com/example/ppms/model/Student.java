package com.example.ppms.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	private String matricNum;
	private String courseCode;
	
	@OneToOne
		@JoinColumn(name = "academic_session_id", referencedColumnName = "academic_session_id", nullable = false)
	private AcademicSession academicSession;

	public Student() {
		super();
	}

	public User getUser() {
		return user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMatricNum() {
		return matricNum;
	}

	public void setMatricNum(String matricNum) {
		this.matricNum = matricNum;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AcademicSession getAcademicSession() {
		return academicSession;
	}

	public void setAcademicSession(AcademicSession academicSession) {
		this.academicSession = academicSession;
	}

	public Student(User user, String matricNum, String courseCode, AcademicSession academicSession) {
		this.user = user;
		this.matricNum = matricNum;
		this.courseCode = courseCode;
		this.academicSession = academicSession;
	}

}
