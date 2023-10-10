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
@Table(name = "lecturers")
public class Lecturer extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	private String staffId;
	private boolean isProgrammeManager;
	
	public Lecturer() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public boolean isProgrammeManager() {
		return isProgrammeManager;
	}

	public void setProgrammeManager(boolean isProgrammeManager) {
		this.isProgrammeManager = isProgrammeManager;
	}

	public Lecturer(User user, String staffId, boolean isProgrammeManager) {
		this.user = user;
		this.staffId = staffId;
		this.isProgrammeManager = isProgrammeManager;
	}
	
}
