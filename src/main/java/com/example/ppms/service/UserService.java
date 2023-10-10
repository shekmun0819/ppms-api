package com.example.ppms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.ppms.jwt.RegisterRequest;

//import org.springframework.stereotype.Service;

import com.example.ppms.model.User;

public interface UserService {
	
	public User saveUser(User user);
	public void importUser(RegisterRequest[] users);
	public void updateUser(RegisterRequest user);
	public void activateUser(List<String> ids);
	public void deactivateUser(List<String> ids);
	public List<User> getAllUsers();
	public Map<String,Object> getUserSelection();
	public Map<String,Object> getUser(String id);
	public User getUserbyEmailAndPassword(String email, String password) /*throws UserNotFoundException*/;
	User getUserByEmailAndPassword(String email, String password);
}
