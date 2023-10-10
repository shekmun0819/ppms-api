package com.example.ppms.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.jsonModel.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
	
	//@Autowired
	private final AuthenticationService service;
	
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
		System.out.println("register authentication controller");
		System.out.println(request);
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) throws Exception{
		System.out.println(request);
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PutMapping("/changePwd")
	public String changePassword(@RequestBody ChangePasswordRequest request){
		service.changePassword(request);
		return "Password is updated";
	}

	@PutMapping("/resetPwd/{id}")
	public String resetPassword(@PathVariable("id") String id){
		service.resetPassword(id);
		return "Password is reset";
	}

}
