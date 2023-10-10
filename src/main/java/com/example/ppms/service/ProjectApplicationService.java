package com.example.ppms.service;

import java.util.List;

import com.example.ppms.model.ProjectApplication;

public interface ProjectApplicationService {

	public ProjectApplication saveProjectApplication(ProjectApplication projectApplication);
	
	public List<ProjectApplication> getProjectApplicationByStudentId(String studentid);
	
	public List<Object>getProjectApplicationByHostId(String hostid);
	
	public void acceptApplication(String applicationId);
	
	public void rejectApplication(String applicationId);

	public void acceptOffer(String applicationId);
	
	public void rejectOffer(String applicationId);

	public String checkApplication(String student_id, String project_id);
}
