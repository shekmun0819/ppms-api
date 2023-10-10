package com.example.ppms.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.jsonModel.AddAssessorRequest;
import com.example.ppms.jsonModel.AddPracticumProjectRequest;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface PracticumProjectService {

	public List<PracticumProject> getAllPracticumProjects();

	public List<PracticumProject> getAllProjectsByAcademicSession(int acadId);

	public List<PracticumProject> getAllProjectsByAcademicSessionCourse(int acadId, String json);

	// public List<PracticumProject> getAllOpenProjects();

	public List<PracticumProject> getAllOpenAndActiveProjects();

	// public PracticumProject savePracticumProject(PracticumProject
	// practicumProject);

	public PracticumProject savePracticumProject(PracticumProject practicumProject, MultipartFile image,
			MultipartFile nda)
			throws Exception;

	public Map<String, Object> getPracticumProject(String id);

	public void updatePracticumProject(PracticumProject editProject, MultipartFile image, MultipartFile nda)
			throws Exception;

	public void updateProjectAssessor(int projId, int supId, int exaId, int panelId, int chairId);

	public String deletePracticumProject(String id);

	public List<PracticumProject> getPracticumProjectByHostId(String userId);

	public Map<String, Object> getPracticumProjectDetails(int id);

	public void importAssessor(AddAssessorRequest[] assessors, int acadId);

	public Map<String, List<Object>> getPracticumProjectBySupervisorId(int supId);

	public Map<String, List<Object>> getPracticumProjectByExaminerId(int exaId);

	public Map<String, List<Object>> getPracticumProjectByPanelId(int panelId);

	public Map<String, List<Object>> getPracticumProjectByHostId(int hostId);

	public String checkStudent(String student_id);

	public Map<String, Integer> getProjectCategoryData(int acadId);

	public List<PracticumProject> getProjectRecommendation(String project_id) throws IOException, InterruptedException;

	public void savePracticumProject(AddPracticumProjectRequest practicumProject) throws JsonMappingException, JsonProcessingException;

	public void deactivatePracticumProject(AddPracticumProjectRequest practicumProject);

	public void updatePracticumProject(AddPracticumProjectRequest practicumProject);

	public PracticumProject downloadNDA(String id);
}
