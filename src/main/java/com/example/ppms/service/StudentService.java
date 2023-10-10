package com.example.ppms.service;

import java.util.List;
import java.util.Map;

import com.example.ppms.model.Student;

public interface StudentService {

	public List<Student> getAllStudents();

	public List<Object> getAllStudentsAndPracticumProject();

	public Student getStudentDetails(int userId);

	public Map<String, Integer> getAllStudentsByCourse();

	// public List<Student> getAllStudentsAndPracticumProject();
}
