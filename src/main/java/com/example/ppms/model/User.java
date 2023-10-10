package com.example.ppms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(unique=true)
	private String email;
	
	private String password;
	private String roles;
	private boolean active;

	public User() {
		super();
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String email, String name, String roles, int id) {
		this.email = email;
		this.name = name;
		this.roles = roles;
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	// need to double check, modified before
	/*
	 * public Collection<? extends GrantedAuthority> getAuthorities() {
	 * 
	 * Collection<GrantedAuthority> result = new ArrayList<>(); for(String role :
	 * getRole()) { result.add(new SimpleGrantedAuthority(role)); }
	 * 
	 * return result; }
	 */

	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> listRoles = new ArrayList<GrantedAuthority>();

		listRoles.add(new SimpleGrantedAuthority(roles)); // this is the problematic
		return listRoles;
		// return Collections.emptyList();
		// return List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email; // our username is the email
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
