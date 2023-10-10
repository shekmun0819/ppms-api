package com.example.ppms.jwt;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.ChangePasswordRequest;
import com.example.ppms.model.Host;
import com.example.ppms.model.Lecturer;
import com.example.ppms.model.Student;
import com.example.ppms.model.User;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.CompanyRepository;
import com.example.ppms.repository.HostRepository;
import com.example.ppms.repository.LecturerRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository repository;
	private final StudentRepository studentRepository;
	private final LecturerRepository lecturerRepository;
	private final HostRepository hostRepository;
	private final CompanyRepository companyRepository;
  private final AcademicSessionRepository academicSessionRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
	
	@Autowired
  private JavaMailSender javaMailSender;

	public AuthenticationResponse register(RegisterRequest request) {
		
		var academicSession = academicSessionRepository.findByAcademicSessionAndSemester(request.getAcademicSession(), request.getSemester());

		//random number
		//String pwd = RandomStringUtils.random( 10, characters );
		String pwd = "12345";

		var user = User.builder()
				.email(request.getEmail())
				.password(passwordEncoder.encode(pwd))
				.name(request.getName().toUpperCase())
				.roles(request.getRoles())
				.active(true)
				.build();

		User newUser = repository.save(user);

		if(newUser.getRoles().contains("Student")) {
			Student newStudent = new Student(newUser, request.getMatricNum(), request.getCourseCode().toUpperCase(), academicSession.get());
			studentRepository.save(newStudent);
		}
		if(newUser.getRoles().contains("Lecturer")) {
			if(newUser.getRoles().contains("Admin")) {
				Lecturer newLecturer = new Lecturer(newUser, request.getStaffId(), true);
				lecturerRepository.save(newLecturer);
			}
			else {
				Lecturer newLecturer = new Lecturer(newUser, request.getStaffId(), false);
				lecturerRepository.save(newLecturer);
			}
		}
		if(newUser.getRoles().contains("Host")) {
			var company = companyRepository.findById(request.getCompanyId());
			Host newHost= new Host(newUser, company.get());
			hostRepository.save(newHost);
		}

		//Send Email
		// SimpleMailMessage msg = new SimpleMailMessage();
		// msg.setTo(user.getEmail());
		// msg.setSubject("PPMS Temporary Login Credentials");
		// msg.setText("Email: Your USM Email\nPassword: " + pwd + "\n\nPlease login into your account and change your password in Profile page.\n\nThank You.");
		// javaMailSender.send(msg);

		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
		System.out.println(request);
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		if (!user.isActive()) {
			throw new Exception("Your account is deactivated. Please contact system administrator.");
		}
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public void changePassword(ChangePasswordRequest request) {
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		
		var authenticated = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPwd()));
		
		if(authenticated != null) {
			//Hashed new password
			var newPwd = passwordEncoder.encode(request.getNewPwd());
			repository.findById(user.getId()).ifPresent(user1 -> {
				user1.setPassword(newPwd);
				repository.save(user1);
			});
		}
	}

	public void resetPassword(String id) {
		Integer userId = Integer.parseInt(id);
		var user = repository.findById(userId);

		//random number
		//String pwd = RandomStringUtils.random( 10, characters );
		String pwd = "12345";
		
		//Hashed new password
		var newPwd = passwordEncoder.encode(pwd);

		repository.findById(userId).ifPresent(user1 -> {
			user1.setPassword(newPwd);
			repository.save(user1);
		});
		
		//Send Email
		// SimpleMailMessage msg = new SimpleMailMessage();
		// msg.setTo(user.get().getEmail());
		// msg.setSubject("PPMS Temporary Login Credentials");
		// msg.setText("Email: Your USM Email\nPassword: " + pwd + "\n\nPlease login into your account and change your password in Profile page.\n\nThank You.");
		// javaMailSender.send(msg);
	}
}
