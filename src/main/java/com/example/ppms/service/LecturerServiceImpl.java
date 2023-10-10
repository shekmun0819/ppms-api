package com.example.ppms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ppms.model.Lecturer;
import com.example.ppms.repository.LecturerRepository;

@Service
public class LecturerServiceImpl implements LecturerService {

	@Autowired
	private LecturerRepository lecturerRepository;

	@Override
	public List<Lecturer> getAllLecturers() {
		return lecturerRepository.findAll();
	}
	
}
