package com.example.ppms.service;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.example.ppms.jsonModel.AddAssessorRequest;
import com.example.ppms.jsonModel.AddPracticumProjectRequest;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.ProjectApplication;
import com.example.ppms.model.ReportCategory;
import com.example.ppms.model.Student;
import com.example.ppms.model.User;
import com.example.ppms.model.AcademicSession;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.HostRepository;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.repository.ProjectApplicationRepository;
import com.example.ppms.repository.ReportCategoryRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONArray;
//import org.json.simple.JSONObject;
import org.json.JSONObject;

import io.jsonwebtoken.io.IOException;

@Service
public class PracticumProjectServiceImpl implements PracticumProjectService {

	@Autowired
	private PracticumProjectRepository practicumProjectRepository;

	@Autowired
	private HostRepository hostRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReportCategoryRepository reportCategoryRepository;

	@Autowired
	private ProjectApplicationRepository projectApplicationRepository;

	@Override
	public List<PracticumProject> getAllPracticumProjects() {
		return practicumProjectRepository.findAll();
	}

	@Override
	public List<PracticumProject> getAllProjectsByAcademicSession(int acadId) {
		return practicumProjectRepository.findAllByAcademicSessionIdAndStudentIsNotNull(acadId);
	}

	public List<PracticumProject> getAllProjectsByAcademicSessionCourse(int acadId, String json) {

		var temp = json.split(":");
		temp[1] = temp[1].replace("{", "").replace("}", "").replace(" ", "").replace("\"", "");

		List<PracticumProject> projects = new ArrayList<>();
		List<PracticumProject> filteredProjects = new ArrayList<>();
		projects = practicumProjectRepository.findAllByAcademicSessionId(acadId);

		String courseCode = temp[1];
		for (PracticumProject project : projects) {
			if (project.getStudent() != null) {
				var student = studentRepository.findByUserId(project.getStudent().getId());
				if (courseCode.contains(student.getCourseCode())) {
					filteredProjects.add(project);
				}
			}
		}

		return filteredProjects;
	}

	public List<PracticumProject> getAllOpenAndActiveProjects() {
		List<PracticumProject> openProject = practicumProjectRepository.findAllByStatusAndAcademicSession_active("Open",
				true);

		return openProject;
	}

	public PracticumProject savePracticumProject(PracticumProject practicumProject, MultipartFile image,
			MultipartFile nda) throws Exception {

		String imageName = StringUtils.cleanPath(image.getOriginalFilename());
		AcademicSession academicSession = academicSessionRepository.findByActiveTrue().get();

		try {
			if (imageName.contains("..")) {
				throw new Exception("imageName contains invalid path sequence" + imageName);
			}
			if (nda != null) {
				String ndaName = StringUtils.cleanPath(nda.getOriginalFilename());
				practicumProject.setNdaName(ndaName);
				practicumProject.setNdaData(nda.getBytes());
			}
			practicumProject.setImageName(imageName);
			practicumProject.setImageData(image.getBytes());
			practicumProject.setAcademicSession(academicSession);

			return practicumProjectRepository.save(practicumProject);

		} catch (Exception e) {
			throw new Exception("Could not save image" + imageName);
		}

	}

	public List<PracticumProject> getPracticumProjectByHostId(String userId) {
		Integer userid = Integer.parseInt(userId);
		return practicumProjectRepository.findAllByHostId(userid);
	}

	/*
	 * public Optional<PracticumProject> getPracticumProject(String id) {
	 * Integer projectId = Integer.parseInt(id);
	 * return practicumProjectRepository.findById(projectId);
	 * }
	 */

	public Map<String, Object> getPracticumProject(String id) {
		Map<String, Object> map = new HashMap<>();
		Integer projectId = Integer.parseInt(id);

		map.put("practicumProject", practicumProjectRepository.findById(projectId));
		Integer userId = practicumProjectRepository.findById(projectId).get().getHost().getId();
		map.put("host", hostRepository.findAllByUserId(userId));
		// System.out.println(map);
		// System.out.println(map);
		return map;
	}

	public void updatePracticumProject(PracticumProject editProject, MultipartFile image, MultipartFile nda)
			throws Exception {

		String imageName;
		byte[] imageData;
		Optional<PracticumProject> findProject = practicumProjectRepository.findById(editProject.getId());
		// System.out.print(findProject.get().getImageName());
		// System.out.print(editProject.getImageName());
		if (findProject.get().getImageName().equals(editProject.getImageName())) {
			// no update image

			imageData = findProject.get().getImageData();
			imageName = editProject.getImageName();

		} else {
			// got update image
			imageName = StringUtils.cleanPath(image.getOriginalFilename());
			imageData = image.getBytes();
		}

		practicumProjectRepository.findById(editProject.getId()).ifPresent(editProject1 -> {
			try {
				if (imageName.contains("..")) {
					throw new Exception("imageName contains invalid path sequence " + imageName);
				}
				if (nda != null ) {
					//nda got change
					if(!nda.getOriginalFilename().equals(editProject1.getNdaName())){
						editProject1.setNdaData(nda.getBytes());
					}
					//nda no change //nda data remain as previous one
					editProject1.setNDA(true);
					//editProject1.setNdaData(nda.getBytes());
					String ndaName = StringUtils.cleanPath(nda.getOriginalFilename());
					editProject1.setNdaName(ndaName);
					
				} else { // remove nda
					editProject1.setNDA(false);
					editProject1.setNdaName(null);
					editProject1.setNdaData(null);
				}

				editProject1.setStatus(editProject.getStatus());
				// editProject1.setAcademicSession(editProject.getAcademicSession());
				editProject1.setAcademicSession(findProject.get().getAcademicSession());
				if (editProject.getStudent() != null) {
					editProject1.setStudent(editProject.getStudent());
				}
				editProject1.setName(editProject.getName());
				editProject1.setDescription(editProject.getDescription());
				editProject1.setImageName(imageName);
				editProject1.setImageData(imageData);
				editProject1.setCategories(editProject.getCategories());
				editProject1.setAdminDescription(editProject.getAdminDescription());
				practicumProjectRepository.save(editProject1);
			} catch (Exception e) {
				try {
					throw new Exception("Could not update project");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
	}

	@Override
	public Map<String, Object> getPracticumProjectDetails(int id) {
		Map<String, Object> map = new HashMap<>();
		PracticumProject project = practicumProjectRepository.findById(id).get();
		int studentUserId = project.getStudent().getId();
		Student student = studentRepository.findByUserId(studentUserId);

		map.put("practicumProject", project);
		map.put("student", student);

		return map;
	}

	@Override
	public void importAssessor(AddAssessorRequest[] assessors, int acadId) {

		var users = userService.getAllUsers();
		var projects = practicumProjectRepository.findAllByAcademicSessionId(acadId);

		for (AddAssessorRequest a : assessors) {
			PracticumProject project = null;
			User supervisor = new User();
			User examiner = new User();
			User panel = new User();
			User chair = new User();

			for (int i = 0; i < projects.size(); i++) {
				if ((projects.get(i).getName()).equals(a.getTitle())) {
					project = projects.get(i);
					break;
				}
			}

			if (project == null) {
				continue;
			}

			for (int i = 0; i < users.size(); i++) {
				if ((users.get(i).getName()).equals(a.getSupervisor().toUpperCase())) {
					supervisor = users.get(i);
					project.setSupervisor(supervisor);
					break;
				}
			}

			for (int i = 0; i < users.size(); i++) {
				if ((users.get(i).getName()).equals(a.getExaminer().toUpperCase())) {
					examiner = users.get(i);
					project.setExaminer(examiner);
					break;
				}
			}

			for (int i = 0; i < users.size(); i++) {
				if ((users.get(i).getName()).equals(a.getPanel().toUpperCase())) {
					panel = users.get(i);
					project.setPanel(panel);
					break;
				}
			}

			for (int i = 0; i < users.size(); i++) {
				if ((users.get(i).getName()).equals(a.getChair().toUpperCase())) {
					chair = users.get(i);
					project.setChair(chair);
					break;
				}
			}

			practicumProjectRepository.save(project);
		}
	}

	@Override
	public void updateProjectAssessor(int projId, int supId, int exaId, int panelId, int chairId) {

		PracticumProject project = practicumProjectRepository.findById(projId).get();

		if (supId > 0) {
			User supervisor = userRepository.findById(supId).get();
			project.setSupervisor(supervisor);
		} else {
			project.setSupervisor(null);
		}

		if (exaId > 0) {
			User examiner = userRepository.findById(exaId).get();
			project.setExaminer(examiner);
		} else {
			project.setExaminer(null);
		}

		if (panelId > 0) {
			User panel = userRepository.findById(panelId).get();
			project.setPanel(panel);
		} else {
			project.setPanel(null);
		}

		if (chairId > 0) {
			User chair = userRepository.findById(chairId).get();
			project.setChair(chair);
		} else {
			project.setChair(null);
		}

		practicumProjectRepository.save(project);

		// User supervisor = userRepository.findById(supId);
		// User examiner = userRepository.findById(exaId).get();
		// User panel = userRepository.findById(panelId).get();

		// practicumProjectRepository.findById(projId).ifPresent(practicumproject1 -> {
		// practicumproject1.setSupervisor(supervisor);
		// practicumproject1.setExaminer(examiner);
		// practicumproject1.setPanel(panel);
		// practicumProjectRepository.save(practicumproject1);
		// });
	}

	@Override
	public Map<String, List<Object>> getPracticumProjectBySupervisorId(int supId) {

		Map<String, List<Object>> map = new HashMap<>();

		List<Object> projects = practicumProjectRepository.findAllBySupervisorIdAndStudentIsNotNull(supId);
		List<Object> studentDetails = new ArrayList<Object>();

		for (Object project : projects) {
			int userId = ((PracticumProject) project).getStudent().getId();
			Student foundStudent = studentService.getStudentDetails(userId);

			studentDetails.add(foundStudent);
		}

		map.put("projects", projects);
		map.put("studentDetails", studentDetails);

		return map;
	}

	@Override
	public Map<String, List<Object>> getPracticumProjectByExaminerId(int exaId) {
		Map<String, List<Object>> map = new HashMap<>();

		List<Object> projects = practicumProjectRepository.findAllByExaminerIdAndStudentIsNotNull(exaId);
		List<Object> studentDetails = new ArrayList<Object>();

		for (Object project : projects) {
			int userId = ((PracticumProject) project).getStudent().getId();
			Student foundStudent = studentService.getStudentDetails(userId);

			studentDetails.add(foundStudent);
		}

		map.put("projects", projects);
		map.put("studentDetails", studentDetails);

		return map;
	}

	@Override
	public Map<String, List<Object>> getPracticumProjectByPanelId(int panelId) {
		Map<String, List<Object>> map = new HashMap<>();

		List<Object> projects = practicumProjectRepository.findAllByPanelIdAndStudentIsNotNull(panelId);
		List<Object> studentDetails = new ArrayList<Object>();

		for (Object project : projects) {
			int userId = ((PracticumProject) project).getStudent().getId();
			Student foundStudent = studentService.getStudentDetails(userId);

			studentDetails.add(foundStudent);
		}

		map.put("projects", projects);
		map.put("studentDetails", studentDetails);

		return map;
	}

	@Override
	public Map<String, List<Object>> getPracticumProjectByHostId(int hostId) {
		Map<String, List<Object>> map = new HashMap<>();

		List<Object> projects = practicumProjectRepository.findAllByHostIdAndStudentIsNotNull(hostId);
		List<Object> studentDetails = new ArrayList<Object>();

		for (Object project : projects) {
			int userId = ((PracticumProject) project).getStudent().getId();
			Student foundStudent = studentService.getStudentDetails(userId);

			studentDetails.add(foundStudent);
		}

		map.put("projects", projects);
		map.put("studentDetails", studentDetails);

		return map;
	}

	public String deletePracticumProject(String id) {
		Integer projectId = Integer.parseInt(id);

		practicumProjectRepository.findById(projectId).ifPresent(practicumProject1 -> {
			practicumProject1.setStatus("Closed");
			practicumProjectRepository.save(practicumProject1);
		});

		//check if any project application
		List<ProjectApplication> found = projectApplicationRepository
					.findAllByPracticumProjectId(projectId);

		//accepted project will not be checked cause after accepted the project will auto closed
		for (int x = 0; x < found.size(); x++) {
			found.get(x).setStatus("Rejected");
			projectApplicationRepository.save(found.get(x));
		} 

		return "Practicum Project is deleted.";
	}

	boolean get = false;

	public String checkStudent(String student_id) {
		Integer studentId = Integer.parseInt(student_id);

		PracticumProject project = practicumProjectRepository.findByStudentId(studentId);

		if (project != null) {
			get = true;
			return "true";
		} else {
			return "false";
		}
	}

	public List<PracticumProject> getProjectRecommendation(String project_id)
			throws java.io.IOException, InterruptedException {
		System.out.println("Here");
		String apiUrl = "http://127.0.0.1:5000/recommendation";
		Map<String, Object> map = new HashMap<>();
		List<PracticumProject> recommendProject = new ArrayList<>();
		// set up HttpClient
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// input of API
			String inputData = String.format("{\"input\": \"%s\"}", project_id);
			HttpPost httpPost = new HttpPost(apiUrl);
			// set input data as request JSON
			StringEntity requestEntity = new StringEntity(inputData, ContentType.APPLICATION_JSON);
			httpPost.setEntity(requestEntity);

			// execute the request
			HttpResponse response = httpClient.execute(httpPost);

			// read the response
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				String responseBody = EntityUtils.toString(responseEntity);
				JSONObject data = new JSONObject(responseBody);
				JSONArray array = data.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					Integer projectId = Integer.parseInt(array.get(i).toString());
					Optional<PracticumProject> projects = practicumProjectRepository.findById(projectId);
					if (projects.isPresent()) {
						recommendProject.add(i, projects.get());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recommendProject;
	}

	@Override
	public Map<String, Integer> getProjectCategoryData(int acadId) {
		Map<String, Integer> map = new HashMap<>();

		List<PracticumProject> projects = practicumProjectRepository
				.findAllByAcademicSessionIdAndStudentIsNotNull(acadId);
		List<ReportCategory> categories = reportCategoryRepository.findAll();

		for (ReportCategory category : categories) {
			int count = 0;
			for (PracticumProject project : projects) {
				String catString = project.getCategories();
				if (catString.contains(category.getCategoryName())) {
					count++;
				}
			}

			map.put(category.getCategoryName(), count);
		}

		return map;
	}

	@Override
	public void savePracticumProject(AddPracticumProjectRequest practicumProject)
			throws JsonMappingException, JsonProcessingException {

		PracticumProject newPracticumProject = new PracticumProject();

		// newPracticumProject.setId(practicumProject.getId());
		newPracticumProject.setName(practicumProject.getName());
		newPracticumProject.setCategories(practicumProject.getCategories());
		newPracticumProject.setDescription(practicumProject.getDescription());
		newPracticumProject.setStatus(practicumProject.getStatus());
		newPracticumProject.setAdminDescription(practicumProject.getAdminDescription());
		newPracticumProject.setImageName(practicumProject.getImageName());
		newPracticumProject.setNdaName(practicumProject.getNdaName());
		newPracticumProject.setNDA(practicumProject.isNDA());
		newPracticumProject.setCourseName(practicumProject.getCourseName());
		newPracticumProject.setCourseCode(practicumProject.getCourseCode());
		newPracticumProject.setImageData(practicumProject.getImageData());
		newPracticumProject.setNdaData(practicumProject.getNdaData());
		newPracticumProject.setNdaData(practicumProject.getNdaData());

		AcademicSession academicSession = academicSessionRepository.findById(practicumProject.getAcademicSession())
				.get();
		newPracticumProject.setAcademicSession(academicSession);

		User host = userRepository.findById(practicumProject.getHost()).get();
		newPracticumProject.setHost(host);
		practicumProjectRepository.save(newPracticumProject);
	}

	public void deactivatePracticumProject(AddPracticumProjectRequest practicumProject) {

		practicumProjectRepository.findById(practicumProject.getId()).ifPresent(practicumProject1 -> {
			practicumProject1.setStatus(practicumProject.getStatus());
			practicumProjectRepository.save(practicumProject1);
		});

	}

	public void updatePracticumProject(AddPracticumProjectRequest practicumProject) {
		practicumProjectRepository.findById(practicumProject.getId()).ifPresent(practicumProject1 -> {
			practicumProject1.setCategories(practicumProject.getCategories());
			practicumProject1.setDescription(practicumProject.getDescription());
			practicumProjectRepository.save(practicumProject1);
		});
	}

	public PracticumProject downloadNDA(String id){
		Integer projectId = Integer.parseInt(id);
		return practicumProjectRepository.findById(projectId).get();
	}

}