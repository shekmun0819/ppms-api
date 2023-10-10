package com.example.ppms.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Csminds")
public class Csmind extends BaseEntity {

	@Id
	@Column(name = "csmind_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String courseCode;
	private Date startDate;
	private Date endDate;
	private String periodSlot;
	private int numOfPresentations;

	@OneToOne
		@JoinColumn(name = "academic_session_id", referencedColumnName = "academic_session_id", nullable = false)
	private AcademicSession academicSession;
	
	@Column(columnDefinition = "JSON")
	private String schedule;
	
	public Csmind() {
		super();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public int getNumOfPresentations() {
		return numOfPresentations;
	}
	
	public void setNumOfPresentations(int numOfPresentations) {
		this.numOfPresentations = numOfPresentations;
	}
	
	public String getSchedule() {
		return schedule;
	}
	
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPeriodSlot() {
		return periodSlot;
	}

	public void setPeriodSlot(String periodSlot) {
		this.periodSlot = periodSlot;
	}

	public AcademicSession getAcademicSession() {
		return academicSession;
	}

	public void setAcademicSession(AcademicSession academicSession) {
		this.academicSession = academicSession;
	}

	public Csmind(String courseCode, AcademicSession academicSession, Date startDate, Date endDate,
			String periodSlot, int numOfPresentations, String schedule) {
		this.courseCode = courseCode;
		this.academicSession = academicSession;
		this.startDate = startDate;
		this.endDate = endDate;
		this.periodSlot = periodSlot;
		this.numOfPresentations = numOfPresentations;
		this.schedule = schedule;
	}
	
}
