package com.example.ppms.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ppms.model.ProjectApplication;
import com.example.ppms.service.ProjectApplicationService;

@RestController
@RequestMapping("api/v1/projectapplication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectApplicationController {
	
	@Autowired
	private ProjectApplicationService projectApplicationService;
	
	@PostMapping("/addprojectapplication")
	public String saveProjectApplication(@RequestBody ProjectApplication projectApplication) {
		projectApplicationService.saveProjectApplication(projectApplication);
		return "New project application is added";
	}
	
	@GetMapping("/getByStudentId/{studentid}")
	public List<ProjectApplication> getProjectApplicationByStudentId(@PathVariable("studentid") String studentid){
		return projectApplicationService.getProjectApplicationByStudentId(studentid);
	}
	
	@GetMapping("/getByHostId/{hostid}")
	public List<Object> getProjectApplicationByHostId(@PathVariable("hostid") String hostid){
		return projectApplicationService.getProjectApplicationByHostId(hostid);
	}
	
	@GetMapping("/acceptApplication/{id}")
	public String acceptApplication(@PathVariable("id") String applicationId) {
		projectApplicationService.acceptApplication(applicationId);
		return "Project application is accepted by the host";
		
	}
	
	@GetMapping("/rejectApplication/{id}")
	public String rejectApplication(@PathVariable("id") String applicationId) {
		projectApplicationService.rejectApplication(applicationId);
		return "Project application is rejected by the host";
	}

	@GetMapping("/acceptOffer/{id}")
	public String acceptOffer(@PathVariable("id") String applicationId){
		projectApplicationService.acceptOffer(applicationId);
		return "Offer is accepted by the student";
	}

	@GetMapping("/rejectOffer/{id}")
	public String rejectOffer(@PathVariable("id") String applicationId){
		projectApplicationService.rejectOffer(applicationId);
		return "Offer is rejected by the student";
	}

	// @GetMapping("/check/{student_id}/{project_id}")
	// //@GetMapping("/check")
	// //public String checkApplication(@RequestParam("student_id")String student_id, @RequestParam("project_id") String project_id ){
	// public String checkApplication(@PathVariable("student_id")String student_id, @PathVariable("project_id") String project_id ){
	// 	System.out.println("here");
	// 	return projectApplicationService.checkApplication(student_id, project_id);
	// }

	@GetMapping("/check/{student_id}/{project_id}")
	public String checkApplication(@PathVariable("student_id")String student_id, @PathVariable("project_id")String project_id){
		return projectApplicationService.checkApplication(student_id, project_id);
	}
	

}
