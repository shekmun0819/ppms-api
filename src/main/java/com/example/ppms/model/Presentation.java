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

@Entity
@Table(name = "Presentations")
public class Presentation extends BaseEntity {

	@Id
	@Column(name = "pres_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String presIdentity;
	private String timeSlot;
	private String day;
	private String date;
	private String venue;
	private String title;
	
	@OneToOne
    @JoinColumn(name = "csmind_id", referencedColumnName = "csmind_id", nullable = false)
	private Csmind csmind;
	
	@OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
	private User student;

	@OneToOne
    @JoinColumn(name = "supervisor_id", referencedColumnName = "user_id", nullable = false)
	private User supervisor;

	@OneToOne
    @JoinColumn(name = "examiner1_id", referencedColumnName = "user_id", nullable = false)
	private User examinerOne;

	@OneToOne
    @JoinColumn(name = "examiner2_id", referencedColumnName = "user_id", nullable = false)
	private User examinerTwo;
	
	@OneToOne
    @JoinColumn(name = "host_id", referencedColumnName = "user_id", nullable = true)
	private User host;

	public Presentation() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPresIdentity() {
		return presIdentity;
	}

	public void setPresIdentity(String presIdentity) {
		this.presIdentity = presIdentity;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Csmind getCsmind() {
		return csmind;
	}

	public void setCsmind(Csmind csmind) {
		this.csmind = csmind;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public User getExaminerOne() {
		return examinerOne;
	}

	public void setExaminerOne(User examinerOne) {
		this.examinerOne = examinerOne;
	}

	public User getExaminerTwo() {
		return examinerTwo;
	}

	public void setExaminerTwo(User examinerTwo) {
		this.examinerTwo = examinerTwo;
	}

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}

	public Presentation(String presIdentity, String title, Csmind csmind, User student,
		User supervisor, User examinerOne, User examinerTwo) {
		this.presIdentity = presIdentity;
		this.title = title;
		this.csmind = csmind;
		this.student = student;
		this.supervisor = supervisor;
		this.examinerOne = examinerOne;
		this.examinerTwo = examinerTwo;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	

}
