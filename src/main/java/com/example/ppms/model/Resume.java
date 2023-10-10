package com.example.ppms.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Resumes")
public class Resume extends BaseEntity {

	@Id
	@Column(name = "resume_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String filename;
	private String imageName;

	private String name;
	private String contact;
	private String email;
	private String linkedinLink;

	@Column(columnDefinition = "LONGTEXT")
	private String aboutMe;

	@Column(columnDefinition = "LONGTEXT")
	private String education;

	@Column(columnDefinition = "LONGTEXT")
	private String experience;

	@Column(columnDefinition = "LONGTEXT")
	private String skill;

	@Column(columnDefinition = "LONGTEXT")
	private String reference;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] resumeData;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] imageData;

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public byte[] getResumeData() {
		return resumeData;
	}

	public void setResumeData(byte[] resumeData) {
		this.resumeData = resumeData;
	}

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
	private User student;

	public Resume() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkedinLink() {
		return linkedinLink;
	}

	public void setLinkedinLink(String linkedinLink) {
		this.linkedinLink = linkedinLink;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
