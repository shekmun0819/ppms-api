package com.example.ppms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.jwt.RegisterRequest;
import com.example.ppms.model.User;
import com.example.ppms.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/add")
	public String add(@RequestBody User user) {
		userService.saveUser(user);
		return "New user is added";
	}

	@PutMapping("/update")
	public String update(@RequestBody RegisterRequest user) {
		userService.updateUser(user);
		return "User is updated";
	}

	@GetMapping("/getAll")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/getUserSelection")
	public Map<String,Object> getUserSelection() {
		return userService.getUserSelection();
	}

	@GetMapping("/{id}")
	Map<String,Object> getUser(@PathVariable("id") String id) {
		return userService.getUser(id);
 	}

	@PostMapping("/import")
	public String importUser(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		RegisterRequest[] users = objectMapper.readValue(json, RegisterRequest[].class);
		userService.importUser(users);
		return "User(s) is imported";
	}

	@PutMapping("/activate/{ids}")
	public String activateUser(@PathVariable("ids") List<String> ids) {
		userService.activateUser(ids);
		return "User(s) is activated";
	}

	@PutMapping("/deactivate/{ids}")
	public String deactivateUser(@PathVariable("ids") List<String> ids) {
		System.out.println(ids);
		userService.deactivateUser(ids);
		return "User(s) is deactivated";
	}
}
