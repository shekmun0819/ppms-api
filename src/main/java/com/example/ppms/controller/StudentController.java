package com.example.ppms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.model.Student;
import com.example.ppms.service.StudentService;

@RestController
@RequestMapping("/api/v1/student")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@GetMapping("/getAll")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

	@GetMapping("getDetails/userId={id}")
	public Student getStudentDetails(@PathVariable("id") int userId) {
		return studentService.getStudentDetails(userId);
	}

	@GetMapping("/getAllStudentAndPracticumProject")
	public List<Object> getAllStudentsAndPracticumProject() {
		// public List<Student> getAllStudentsAndPracticumProject(){
		return studentService.getAllStudentsAndPracticumProject();
	}

	@GetMapping("/getAllStudentsByCourse")
	public Map<String, Integer> getAllStudentsByCourse() {
		return studentService.getAllStudentsByCourse();
	}
}
