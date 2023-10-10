package com.example.ppms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Constraints")
public class Constraint extends BaseEntity {

	@Id
	@Column(name = "constraint_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(columnDefinition = "JSON")
	private String unavailableTime;
	
	@Column(nullable = true)
	private int numOfConsecutiveSlots;

	@Column(nullable = true)
	private boolean preferVenueChange;
	
	@OneToOne
    @JoinColumn(name = "csmind_id", referencedColumnName = "csmind_id", nullable = false)
	private Csmind csmind;
	
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	private String abbr;

	public Constraint() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnavailableTime() {
		return unavailableTime;
	}

	public void setUnavailableTime(String unavailableTime) {
		this.unavailableTime = unavailableTime;
	}

	public int getNumOfConsecutiveSlots() {
		return numOfConsecutiveSlots;
	}

	public void setNumOfConsecutiveSlots(int numOfConsecutiveSlots) {
		this.numOfConsecutiveSlots = numOfConsecutiveSlots;
	}

	public boolean isPreferVenueChange() {
		return preferVenueChange;
	}

	public void setPreferVenueChange(boolean preferVenueChange) {
		this.preferVenueChange = preferVenueChange;
	}

	public Csmind getCsmind() {
		return csmind;
	}

	public void setCsmind(Csmind csmind) {
		this.csmind = csmind;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Constraint(String unavailableTime, int numOfConsecutiveSlots, boolean preferVenueChange, Csmind csmind,
			User user) {
		this.unavailableTime = unavailableTime;
		this.numOfConsecutiveSlots = numOfConsecutiveSlots;
		this.preferVenueChange = preferVenueChange;
		this.csmind = csmind;
		this.user = user;
	}

	public Constraint(String unavailableTime, Csmind csmind, User user) {
		this.unavailableTime = unavailableTime;
		this.csmind = csmind;
		this.user = user;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	
	
}
