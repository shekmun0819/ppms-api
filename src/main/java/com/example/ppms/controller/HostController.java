package com.example.ppms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.Host;
import com.example.ppms.service.HostService;

@RestController
@RequestMapping("api/v1/host")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HostController {

	@Autowired
	private HostService hostService;
	
	@GetMapping("/getAll")
	public List<Host> getAllStudents() {
		return hostService.getAllHosts();
	}
}
