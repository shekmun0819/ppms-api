package com.example.ppms.service;

import org.springframework.stereotype.Service;

import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.ProjectApplication;
import com.example.ppms.model.Student;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.repository.ProjectApplicationRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProjectApplicationServiceImpl implements ProjectApplicationService {

	@Autowired
	private ProjectApplicationRepository projectApplicationRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PracticumProjectRepository practicumProjectRepository;

	@Autowired
	private UserRepository userRepository;

	public ProjectApplication saveProjectApplication(ProjectApplication projectApplication) {
		//do checking whether there is existing "host accept offer"
		Integer practicumProjectId = projectApplication.getPracticumProject().getId();
		List<ProjectApplication> found = projectApplicationRepository.findAllByPracticumProjectId(practicumProjectId);
		for(ProjectApplication x : found){
			if(x.getStatus().equals("Host Accepted")){
				projectApplication.setStatus("On hold");
			}
		}
		return projectApplicationRepository.save(projectApplication);
	}

	public List<ProjectApplication> getProjectApplicationByStudentId(String studentid) {
		Integer studentId = Integer.parseInt(studentid);
		return projectApplicationRepository.findAllByStudentIdAndPracticumProject_AcademicSession_active(studentId, true);
	}

	public List<Object> getProjectApplicationByHostId(String hostid) {
		Integer hostId = Integer.parseInt(hostid);
		List<ProjectApplication> hostProject = projectApplicationRepository.findAll();
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < hostProject.size(); i++) {
			if (hostProject.get(i).getPracticumProject() != null
					&& hostProject.get(i).getPracticumProject().getHost().getId() == hostId && hostProject.get(i).getPracticumProject().getAcademicSession().isActive()) {
				int x = 0;
				Map<String, Object> map = new HashMap<>();
				map.put("projectApplication", hostProject.get(i));
				map.put("student", studentRepository.findAllByUserId(hostProject.get(i).getStudent().getId()));
				list.add(x, map);
				x++;
			}
		}
		return list;
	}

	// host accept one student
	// other student who applied the same project will set in on hold status
	public void acceptApplication(String applicationId) {
		Integer applicationid = Integer.parseInt(applicationId);

		projectApplicationRepository.findById(applicationid).ifPresent(projectApplication -> {
			projectApplication.setStatus("Host Accepted");
			Integer practicumProjectId = projectApplication.getPracticumProject().getId();
			Integer studentId = projectApplication.getStudent().getId();
			projectApplicationRepository.save(projectApplication);

			// practicumProjectRepository.findById(practicumProjectId).ifPresent(practicumProject
			// -> {
			// practicumProject.setStudent(userRepository.getById(studentId));
			// practicumProject.getStudent().getName();
			// practicumProject.setStatus("Closed");
			// practicumProjectRepository.save(practicumProject);
			// });
			List<ProjectApplication> found = projectApplicationRepository
					.findAllByPracticumProjectId(practicumProjectId);
			System.out.println(found);
			for (int x = 0; x < found.size(); x++) {

				if (found.get(x).getId() != applicationid && (!found.get(x).getStatus().equals("Rejected"))) {
					found.get(x).setStatus("On hold");
					System.out.println(found.get(x).getStatus());
					projectApplicationRepository.save(found.get(x));
				}
			}

		});

	}

	// student accept offer from host
	public void acceptOffer(String applicationId) {
		Integer applicationid = Integer.parseInt(applicationId);
		// update status
		projectApplicationRepository.findById(applicationid).ifPresent(projectApplication -> {
			Integer practicumProjectId = projectApplication.getPracticumProject().getId();
			Integer studentId = projectApplication.getStudent().getId();
			projectApplication.setStatus("Accepted");
			projectApplicationRepository.save(projectApplication);
			// get practicum project to update status
			practicumProjectRepository.findById(practicumProjectId).ifPresent(practicumProject -> {
				practicumProject.setStudent(userRepository.getById(studentId));
				practicumProject.setStatus("Closed");
				practicumProjectRepository.save(practicumProject);
			});

			// other project application of same practicum project reject status
			List<ProjectApplication> samePracticumProject = projectApplicationRepository
					.findAllByPracticumProjectId(practicumProjectId);

			if (samePracticumProject.size() != 1) {
				for (ProjectApplication project : samePracticumProject) {
					if (project.getId() != applicationid) {
						project.setStatus("Rejected");
						System.out.println(project.getStatus());
						projectApplicationRepository.save(project);
					}
				}
			}

			// this student who have other application also need to reject
			List<ProjectApplication> studentApplication = projectApplicationRepository.findAllByStudentId(studentId);
			if (studentApplication.size() != 1) {
				// for ( int x = 0; x < studentApplication.size(); x++){
				for (ProjectApplication student : studentApplication) {
					if (student.getId() != applicationid) {
						student.setStatus("Rejected");
						System.out.println(student.getStatus());
						projectApplicationRepository.save(student);

						//check other application got other student apply
						//got - update to pending to review
						Integer otherStudentApplicationPracticumProjectId = student.getPracticumProject().getId();
						List<ProjectApplication> otherStudentApplication = projectApplicationRepository.findAllByPracticumProjectId(otherStudentApplicationPracticumProjectId);
						System.out.println("Size: " + otherStudentApplication.size());
						for(ProjectApplication project2: otherStudentApplication){
							System.out.println("Project2"+ + project2.getId());
							if(!project2.getStatus().equals("Rejected")){ //reject one wont be check
								project2.setStatus("Pending to review");
								projectApplicationRepository.save(project2);
							}
						}
					}
				}
			}

		});

	}

	// student reject offer
	public void rejectOffer(String applicationId) {
		// update applicationid -> reject status
		System.out.println(("Reject offer"));
		System.out.println((applicationId));
		Integer applicationid = Integer.parseInt(applicationId);
		// update status
		projectApplicationRepository.findById(applicationid).ifPresent(projectApplication -> {
			System.out.print("found");
			Integer practicumProjectId = projectApplication.getPracticumProject().getId();
			projectApplication.setStatus("Rejected");
			projectApplicationRepository.save(projectApplication);
			// other project application set to pending for review
			// rejected offer will not be updated status
			List<ProjectApplication> found = projectApplicationRepository
					.findAllByPracticumProjectId(practicumProjectId);
			System.out.println(found);
			for (int x = 0; x < found.size(); x++) {

				if (found.get(x).getId() != applicationid && (!found.get(x).getStatus().equals("Rejected"))) {
					found.get(x).setStatus("Pending to review");
					System.out.println(found.get(x).getStatus());
					projectApplicationRepository.save(found.get(x));
				}
			}
		});

	}

	// host who reject application
	public void rejectApplication(String applicationId) {
		Integer applicationid = Integer.parseInt(applicationId);

		projectApplicationRepository.findById(applicationid).ifPresent(projectApplication -> {
			projectApplication.setStatus("Rejected");

			Integer practicumProjectId = projectApplication.getPracticumProject().getId();
			Integer studentId = projectApplication.getStudent().getId();

			projectApplicationRepository.save(projectApplication);

			// practicumProjectRepository.findById(practicumProjectId).ifPresent(practicumProject
			// -> {
			// // practicumProject.setStudent(userRepository.getById(studentId));
			// practicumProjectRepository.save(practicumProject);
			// });
		});
	}

	boolean isFound = false;

	public String checkApplication(String student_id, String project_id) {
		Integer studentId = Integer.parseInt(student_id);
		Integer projectId = Integer.parseInt(project_id);
		isFound = false;

		PracticumProject projectList = practicumProjectRepository.findByStudentId(studentId);
		if(projectList!=null){
			isFound = true;
			return "true";
		}
		List<ProjectApplication> applicationList = projectApplicationRepository.findAllByPracticumProjectId(projectId);
		// System.out.println(projectList.isPresent());
		if (!applicationList.isEmpty()) {
			for (int x = 0; x < applicationList.size(); x++) {
				Integer checkId = applicationList.get(x).getStudent().getId();
				System.out.print("Checkid:" + checkId);
				System.out.print("studentId:" + studentId);
				if (checkId == studentId) {
					isFound = true;
					return "true";
				} /*else {
					isFound = false;
					return "false";
				}*/
			}
		}
		return "false";
	}
}