package com.example.ppms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.ProjectApplication;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Integer> {

	public List<ProjectApplication>findAllByStudentId(Integer studentId);

	public List<ProjectApplication>findAllByStudentIdAndPracticumProject_AcademicSession_active(Integer studentId, Boolean active);

	public List<ProjectApplication> findAllByPracticumProjectId(Integer projectId);

	
	
	//public List<ProjectApplication>projectApplicationRepository.findAllByHostId(Integer hostId);
}
