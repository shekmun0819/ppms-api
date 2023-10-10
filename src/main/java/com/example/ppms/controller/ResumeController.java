package com.example.ppms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.model.Resume;
import com.example.ppms.service.ResumeService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("api/v1/resume")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ResumeController {

	@Autowired
	private ResumeService resumeService;

	@GetMapping("/getAll")
	public List<Resume> getAllResumes() {
		return resumeService.getAllResumes();
	}

	@GetMapping("/getById/{id}")
	public Optional<Resume> getResumeById(@PathVariable("id") String id) {
		return resumeService.getResumeById(id);
	}

	@PostMapping("/upload")
	public String uploadResume(@RequestParam("resume") MultipartFile resume,
			@RequestParam("student_id") String student_id) throws Exception {

		resumeService.uploadResume(resume, student_id);

		return "Save successfully";
	}

	@GetMapping("/getAllByStudentId/{id}")
	public List<Resume> getResumeByStudentId(@PathVariable("id") String id) {
		return resumeService.getResumeByStudentId(id);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadResume(@PathVariable String id) {

		return resumeService.downloadResume(id);
	}

	@PostMapping("/saveResume")
	public String saveResume(@RequestParam("resume") MultipartFile resumeFile, @RequestParam("name") String name,
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("email") String email, @RequestParam("contactNo") String contactNo,
			@RequestParam("linkedInLink") String linkedInLink, @RequestParam("aboutMe") String aboutMe,
			@RequestParam("education") String education, @RequestParam("experience") String experience,
			@RequestParam("reference") String reference, @RequestParam("skill") String skill,
			@RequestParam("student_id") String student_id)
			throws Exception {
		Resume newResume = new Resume();
		newResume.setName(name);
		newResume.setEmail(email);
		newResume.setContact(contactNo);
		newResume.setLinkedinLink(linkedInLink);
		newResume.setAboutMe(aboutMe);
		newResume.setEducation(education);
		newResume.setExperience(experience);
		newResume.setSkill(skill);
		newResume.setReference(reference);
		resumeService.saveResume(resumeFile, photo, newResume, student_id);
		return "Saved";
	}

	@PutMapping("/updateResume")
	public String updateResume(@RequestParam("id") String id, @RequestParam("resume") MultipartFile resumeFile,
			@RequestParam("photo") MultipartFile photo, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("contactNo") String contactNo,
			@RequestParam("linkedInLink") String linkedInLink, @RequestParam("aboutMe") String aboutMe,
			@RequestParam("education") String education, @RequestParam("experience") String experience,
			@RequestParam("reference") String reference, @RequestParam("skill") String skill,
			@RequestParam("student_id") String student_id)
			throws Exception {
		Integer resumeId = Integer.parseInt(id);
		Resume newResume = new Resume();
		newResume.setId(resumeId);
		newResume.setName(name);
		newResume.setEmail(email);
		newResume.setContact(contactNo);
		newResume.setLinkedinLink(linkedInLink);
		newResume.setAboutMe(aboutMe);
		newResume.setEducation(education);
		newResume.setExperience(experience);
		newResume.setReference(reference);
		newResume.setSkill(skill);
		resumeService.updateResume(resumeFile, photo, newResume, student_id);
		return "Saved";
	}

	@GetMapping("/getResumeImage/{id}")
	public ResponseEntity<Resource> getImageData(@PathVariable String id) {
		Resume resume = null;
		resume = resumeService.getResumeById(id).get();
		String extension = "";

		int i = resume.getImageName().lastIndexOf('.');
		if (i > 0) {
			extension = resume.getImageName().substring(i + 1);
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/" + extension))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getImageName() + "\"")
				.body(new ByteArrayResource(resume.getImageData()));
	}

}
