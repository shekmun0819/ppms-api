package com.example.ppms.jwt;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	//private String firstName;
	
	//private String lastName;
	private int id;

	private String name;
	
	private String email;
	
	private String password;
	
	private String roles;

	private String staffId;

	private String matricNum;

	private String courseCode;

	private String academicSession;

	private int semester;

	private int companyId;

	private String companyName;
	
	//private String[] role;
}
