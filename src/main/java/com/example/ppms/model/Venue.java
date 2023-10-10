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
@Table(name = "Venues")
public class Venue extends BaseEntity {

	@Id
	@Column(name = "venue_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private String abbr;
	
	@Column(columnDefinition = "JSON")
	private String unavailableTime;
	
	@OneToOne
    @JoinColumn(name = "csmind_id", referencedColumnName = "csmind_id", nullable = false)
	private Csmind csmind;

	public Venue() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnavailableTime() {
		return unavailableTime;
	}

	public void setUnavailableTime(String unavailableTime) {
		this.unavailableTime = unavailableTime;
	}

	public Csmind getCsmind() {
		return csmind;
	}

	public void setCsmind(Csmind csmind) {
		this.csmind = csmind;
	}

	public Venue(String name, Csmind csmind) {
		this.name = name;
		this.csmind = csmind;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
}
