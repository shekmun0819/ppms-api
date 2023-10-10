package com.example.ppms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.Lecturer;
import com.example.ppms.service.LecturerService;

@RestController
@RequestMapping("/lecturer")
public class LecturerController {

	@Autowired
	private LecturerService lecturerService;
	
	@GetMapping("/getAll")
	public List<Lecturer> getAllStudents() {
		return lecturerService.getAllLecturers();
	}
}
