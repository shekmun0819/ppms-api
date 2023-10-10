package com.example.ppms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.Student;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PracticumProjectRepository practicumProjectRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Override
	public List<Student> getAllStudents() {

		return studentRepository.findAll();
	}

	@Override
	public Student getStudentDetails(int userId) {
		return studentRepository.findByUserId(userId);
	}

	public List<Object> getAllStudentsAndPracticumProject() {
		List<Object> studentList = new ArrayList<>();

		//List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC, "user_name"));
		List<Student> students = studentRepository.findAllByAcademicSession_active( true, Sort.by(Sort.Direction.DESC, "user_name"));

		for (Student student : students) {
			int i = 0;
			Map<String, Object> map = new HashMap<>();
			map.put("student", student);
			Integer studentId = student.getUser().getId();
			map.put("practicumProject", practicumProjectRepository.findAllByStudentId(studentId));
			studentList.add(i, map);
			i++;
		}

		// return studentRepository.findAllStudentsAndPracticumProject();
		return studentList;
	}

	public Optional<Student> getStudentByUserId(Integer userId) {
		return studentRepository.findAllByUserId(userId);
	}

	@Override
	public Map<String, Integer> getAllStudentsByCourse() {

		Map<String, Integer> map = new HashMap<>();
		int currentAcadId = 0;
		int totalCDS590Students = 0;
		int totalCDT594Students = 0;

		boolean currentAcadSessionExist = academicSessionRepository.findByActiveTrue().isPresent();

		if (currentAcadSessionExist) {
			currentAcadId = academicSessionRepository.findByActiveTrue().get().getId();
			totalCDS590Students = studentRepository.countByCourseCodeAndAcademicSessionId("CDS590", currentAcadId);
			totalCDT594Students = studentRepository.countByCourseCodeAndAcademicSessionId("CDT594", currentAcadId);
		}

		map.put("CDS590", totalCDS590Students);
		map.put("CDT594", totalCDT594Students);

		return map;
	}
}
