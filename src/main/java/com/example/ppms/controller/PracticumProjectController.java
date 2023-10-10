package com.example.ppms.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.jsonModel.AddAssessorRequest;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.Report;
import com.example.ppms.repository.UserRepository;
import com.example.ppms.service.PracticumProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/v1/practicumProjects")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PracticumProjectController {

	@Autowired
	private PracticumProjectService practicumProjectService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/getAll")
	public List<PracticumProject> getAllPracticumProjects() {
		return practicumProjectService.getAllPracticumProjects();
	}

	@GetMapping("/getAll/academicSessionId={acadId}")
	public List<PracticumProject> getAllProjectsByAcademicSession(@PathVariable("acadId") int acadId) {
		return practicumProjectService.getAllProjectsByAcademicSession(acadId);
	}

	@PutMapping("/getAll/academicSessionId={acadId}")
	public List<PracticumProject> getAllProjectsByAcademicSessionCourse(@PathVariable("acadId") int acadId,
			@RequestBody String json) {
		return practicumProjectService.getAllProjectsByAcademicSessionCourse(acadId, json);
	}

	@GetMapping("/getOpenAndActiveProject")
	public List<PracticumProject> getAllOpenAndActiveProjects() {
		return practicumProjectService.getAllOpenAndActiveProjects();
	}

	/*
	 * @PostMapping("/addpracticumproject")
	 * public String add(@RequestBody PracticumProject practicumProject) {
	 * practicumProjectService.savePracticumProject(practicumProject);
	 * return "New practicum project is added";
	 * }
	 */

	@PostMapping("/addpracticumproject")
	public String savePracticumProject(@RequestParam("name") String projectName, @RequestParam("description") String projectDescription,
			@RequestParam("categories") String categories, @RequestParam("adminDescription") String adminDesc,
			@RequestParam("host_id") int hostId, @RequestParam("imageName") String imageName,
			@RequestParam("isNda") Boolean isNda,
			@RequestParam("image") MultipartFile image,
			@RequestParam("course_name") String courseName,
			@RequestParam(value = "nda", required = false) MultipartFile nda) throws Exception {
		PracticumProject newProject = new PracticumProject();
		newProject.setName(projectName);
		newProject.setDescription(projectDescription);
		newProject.setAdminDescription(adminDesc);
		newProject.setCategories(categories);
		newProject.setStatus("Open");
		newProject.setImageName(imageName);
		newProject.setNDA(isNda);
		newProject.setCourseName(courseName);
		if (courseName.equals("Digital Transformation")) {
			newProject.setCourseCode("CDT594");
		} else { // data science and analytic
			newProject.setCourseCode("CDS590");
		}
		newProject.setHost(userRepository.getOne(hostId));
		practicumProjectService.savePracticumProject(newProject, image, nda);

		return "New practicum project is added";
	}

	@GetMapping("/{id}")
	Map<String, Object> getPracticumProject(@PathVariable("id") String id) {
		return practicumProjectService.getPracticumProject(id);
	}
	
	@GetMapping("/getByUserId/{userid}")
	public List<PracticumProject> getPracticumProjectByHostId(@PathVariable("userid") String userId) {
		return practicumProjectService.getPracticumProjectByHostId(userId);
	}

	@GetMapping("/projectId={id}")
	Map<String, Object> getPracticumProjectDetails(@PathVariable("id") int id){
		return practicumProjectService.getPracticumProjectDetails(id);
	}

	@GetMapping("/getProject/supervisor/{supId}")
	public Map<String, List<Object>> getPracticumProjectBySupervisorId(@PathVariable("supId") int supId) {
		return practicumProjectService.getPracticumProjectBySupervisorId(supId);
	}

	@GetMapping("/getProject/examiner/{exaId}")
	public Map<String, List<Object>> getPracticumProjectByExaminerId(@PathVariable("exaId") int exaId) {
		return practicumProjectService.getPracticumProjectByExaminerId(exaId);
	}

	@GetMapping("/getProject/panel/{panelId}")
	public Map<String, List<Object>> getPracticumProjectByPanelId(@PathVariable("panelId") int panelId) {
		return practicumProjectService.getPracticumProjectByPanelId(panelId);
	}

	@GetMapping("/getProject/host/{hostId}")
	public Map<String, List<Object>> getPracticumProjectByHostId(@PathVariable("hostId") int hostId) {
		return practicumProjectService.getPracticumProjectByHostId(hostId);
	}

	@PutMapping("/update")
	public String update(@RequestParam("id") String id, @RequestParam("name") String projectName,
			@RequestParam("description") String projectDescription,
			@RequestParam("categories") String categories, @RequestParam("adminDescription") String adminDesc,
			@RequestParam("isNda") Boolean isNda,
			@RequestParam("status") String status,
			@RequestParam("imageName") String imageName,
			@RequestParam("course_name") String courseName,
			@RequestParam(value = "student_id", required = false) String student_id,
			@RequestParam("academicSession") String academicSession,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "nda", required = false) MultipartFile nda) throws Exception {

		Integer projectId = Integer.parseInt(id);
		//Integer hostId = Integer.parseInt(host_id);

		// System.out.println("studentid " + student_id);
		PracticumProject editProject = new PracticumProject();
		editProject.setId(projectId);
		editProject.setName(projectName);
		editProject.setDescription(projectDescription);
		editProject.setAdminDescription(adminDesc);
		editProject.setCategories(categories);
		editProject.setStatus(status);
		editProject.setImageName(imageName);
		if (courseName.equals("DIGITAL TRANSFORMATION")) { 
			editProject.setCourseCode("CDT594");
		} else { // data science and analytic
			editProject.setCourseCode("CDS590");
		}
		if (student_id.equals("null") || student_id.equals(null)) {

		} else {
			// System.out.println("enter here"); //not null
			Integer studentId = Integer.parseInt(student_id);
			editProject.setStudent(userRepository.getOne(studentId));
		}
		// editProject.setAcademicSession(academicSession);
		editProject.setNDA(isNda);
		practicumProjectService.updatePracticumProject(editProject, image, nda);

		return "practicum project is edited";
	}

	@PutMapping("/updateAssessor/{projId}/supervisorId={supId}&examinerId={exaId}&panelId={panelId}&chairId={chairId}")
	public String updateProjectAssessor(@PathVariable("projId") int projId,
			@PathVariable("supId") int supId,
			@PathVariable("exaId") int exaId,
			@PathVariable("panelId") int panelId,
			@PathVariable("chairId") int chairId) {
		practicumProjectService.updateProjectAssessor(projId, supId, exaId, panelId, chairId);
		return "Practicum Project is updated";

	}

	@PutMapping("/delete/{id}")
	public String deletePracticumProject(@PathVariable("id") String id) {
		return practicumProjectService.deletePracticumProject(id);
	}

	@PostMapping("/academicSessionId={acadId}/import")
	public String importAssessor(@RequestBody String json, @PathVariable("acadId") int acadId)
			throws JsonMappingException, JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		AddAssessorRequest[] assessors = objectMapper.readValue(json, AddAssessorRequest[].class);
		practicumProjectService.importAssessor(assessors, acadId);
		return "Assessor(s) is imported";
	}

	@GetMapping("/checkStudent/{student_id}")
	public String checkStudent(@PathVariable("student_id") String student_id) {

		return practicumProjectService.checkStudent(student_id);
	}

	@GetMapping("/getProjectCategoryData/academicSessionId={acadId}")
	public Map<String, Integer> getProjectCategoryData(@PathVariable("acadId") int acadId) {
		return practicumProjectService.getProjectCategoryData(acadId);
	}

	@GetMapping("/getRecommendation/{id}")
	public List<PracticumProject> getRecommendation(@PathVariable("id") String id) throws IOException, InterruptedException{
		return practicumProjectService.getProjectRecommendation(id);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
		PracticumProject project = null;
		project = practicumProjectService.downloadNDA(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + project.getNdaName() + "\"")
				.body(new ByteArrayResource(project.getNdaData()));
	}

}