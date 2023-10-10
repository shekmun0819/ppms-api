package com.example.ppms.service;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.example.ppms.jsonModel.AddResumeRequest;
import com.example.ppms.model.Resume;
import com.example.ppms.model.User;
import com.example.ppms.repository.ResumeRepository;
import com.example.ppms.repository.UserRepository;

import org.springframework.util.StringUtils;

@Service
public class ResumeServiceImpl implements ResumeService {

	@Autowired
	private ResumeRepository resumeRepository;

	@Autowired
	private UserRepository studentRepository;

	@Override
	public List<Resume> getAllResumes() {
		return resumeRepository.findAll();
	}

	public void uploadResume(MultipartFile resumeData, String student_id) throws Exception {
		String resumeName = StringUtils.cleanPath(resumeData.getOriginalFilename());
		Resume newResume = new Resume();

		try {
			if (resumeName.contains("..")) {
				throw new Exception("resume name contains invalid path sequence" + resumeName);
			}

			newResume.setResumeData(resumeData.getBytes());
			newResume.setFilename(resumeName);
			Integer id = Integer.parseInt(student_id);
			studentRepository.findById(id).ifPresent(user1 -> {
				newResume.setStudent(user1);
			});

			resumeRepository.save(newResume);
		} catch (Exception e) {
			throw new Exception("could not save file" + resumeName);
		}

	}

	public List<Resume> getResumeByStudentId(String id) {
		Integer userId = Integer.parseInt(id);
		return resumeRepository.findAllByStudentId(userId);
	}

	public ResponseEntity<Resource> downloadResume(String id) {
		Integer resumeId = Integer.parseInt(id);
		Resume resume = new Resume();
		resumeRepository.findById(resumeId).ifPresent(newResume -> {
			resume.setResumeData(newResume.getResumeData());
			resume.setFilename(newResume.getFilename());
		});

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resume.getFilename() + "\"")
				.body(new ByteArrayResource(resume.getResumeData()));
	}

	public void saveResume(MultipartFile resumeFile, MultipartFile photo, Resume newResume, String student_id)
			throws Exception {
		// not yet do save image data
		String resumeName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
		String imangeName = StringUtils.cleanPath(photo.getOriginalFilename());
		System.out.println("Get content type" + resumeFile.getContentType());

		try {
			if (resumeName.contains("..")) {
				throw new Exception("resume name contains invalid path sequence" + resumeName);
			}
			newResume.setImageData(photo.getBytes());
			newResume.setImageName(imangeName);
			newResume.setResumeData(resumeFile.getBytes());
			newResume.setFilename(resumeName);
			Integer studentId = Integer.parseInt(student_id);
			studentRepository.findById(studentId).ifPresent(user1 -> {
				newResume.setStudent(user1);
			});

			resumeRepository.save(newResume);
		} catch (Exception e) {
			throw new Exception("could not save file" + resumeName);
		}

	}

	public Optional<Resume> getResumeById(String id) {
		Integer resumeId = Integer.parseInt(id);
		return resumeRepository.findById(resumeId);
	}

	public void updateResume(MultipartFile resumeFile, MultipartFile photo, Resume resume, String student_id)
			throws Exception {
		// not yet do save image data
		String imageName;
		byte[] imageData;
		String resumeName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
		Optional<Resume> edit = resumeRepository.findById(resume.getId());
		System.out.println("edit" + edit.get().getImageName());
		System.out.println("resume" + resume.getImageName());
		if (edit.get().getImageName().equals(resume.getImageName())) {
			// no update image
			System.out.println("In equal" + photo);
			imageData = edit.get().getImageData();
			imageName = edit.get().getImageName();

		} else {
			// got update image
			imageName = StringUtils.cleanPath(photo.getOriginalFilename());
			imageData = photo.getBytes();
		}

		resumeRepository.findById(resume.getId()).ifPresent(editResume -> {

			try {
				if (resumeName.contains("..")) {
					throw new Exception("resume name contains invalid path sequence" + resumeName);
				}
				editResume.setImageData(photo.getBytes());
				editResume.setResumeData(resumeFile.getBytes());
				editResume.setFilename(resumeName);

				editResume.setName(resume.getName());
				editResume.setContact(resume.getContact());
				editResume.setEmail(resume.getEmail());
				editResume.setLinkedinLink(resume.getLinkedinLink());
				editResume.setAboutMe(resume.getAboutMe());
				editResume.setEducation(resume.getEducation());
				editResume.setExperience(resume.getExperience());
				editResume.setSkill(resume.getSkill());
				editResume.setReference(resume.getReference());
				editResume.setImageData(imageData);
				editResume.setImageName(imageName);

				resumeRepository.save(editResume);

			} catch (Exception e) {
				try {
					throw new Exception("could not save file" + resumeName);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	public void saveResume(AddResumeRequest newResume) throws IOException {

		Resume resume = new Resume();
		//resume.setId(newResume.getId());
		resume.setFilename(newResume.getFilename());
		resume.setImageName(newResume.getImageName());
		resume.setAboutMe(newResume.getAboutMe());
		resume.setName(newResume.getName());
		resume.setContact(newResume.getContact());
		resume.setEmail(newResume.getEmail());
		resume.setEducation(newResume.getEducation());
		resume.setLinkedinLink(newResume.getLinkedinLink());
		resume.setExperience(newResume.getExperience());
		resume.setSkill(newResume.getSkill());
		resume.setReference(newResume.getReference());
		resume.setResumeData(newResume.getResumeData());
		resume.setImageData(newResume.getImageData());

		//User student = studentRepository.findById(newResume.getStudent()).get();
		resume.setStudent(studentRepository.findById(newResume.getStudent()).get());

		resumeRepository.save(resume);
	}

	public void updateResume(AddResumeRequest resume) {
		resumeRepository.findById(resume.getId()).ifPresent(resume1 -> {
			resume1.setAboutMe(resume.getAboutMe());
			resume1.setReference(resume.getReference());
			resumeRepository.save(resume1);

		});

	}

}
