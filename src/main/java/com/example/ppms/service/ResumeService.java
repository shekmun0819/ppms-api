package com.example.ppms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.jsonModel.AddResumeRequest;
import com.example.ppms.model.Resume;

public interface ResumeService {

	public List<Resume> getAllResumes();

	public void uploadResume(MultipartFile resume, String student_id) throws Exception;

	public List<Resume> getResumeByStudentId(String id);

	public ResponseEntity<Resource> downloadResume(String id);

	public void saveResume(MultipartFile resumeFile, MultipartFile photo, Resume newResume, String student_id)throws Exception ;

	public Optional<Resume> getResumeById(String id);

	public void updateResume(MultipartFile resumeFile, MultipartFile photo, Resume newResume, String student_id)throws Exception;

	public void saveResume(AddResumeRequest newResume) throws IOException;

	public void updateResume(AddResumeRequest resume);
}
