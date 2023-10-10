package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.jsonModel.AddResumeRequest;
import com.example.ppms.repository.ResumeRepository;
import com.example.ppms.service.ResumeService;

@SpringBootTest
public class ResumeServiceImplTests {

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    ResumeService resumeService;

    @Test
    void TestCreateResume() throws IOException {
        final MultipartFile mockResumeFile = mock(MultipartFile.class);
        when(mockResumeFile.getOriginalFilename()).thenReturn("resume.pdf");

        final MultipartFile mockImageFile = mock(MultipartFile.class);
        when(mockImageFile.getOriginalFilename()).thenReturn("image.jpg");

        AddResumeRequest newResume = new AddResumeRequest();
        //newResume.setId(23);
        newResume.setStudent(65);
        newResume.setFilename(mockResumeFile.getOriginalFilename());
        newResume.setImageName(mockImageFile.getOriginalFilename());
        newResume.setAboutMe("A passionate data scientist who would like to find practicum regarding Education");
        newResume.setName("Tan Kar Hui");
        newResume.setContact("012-3891293");
        newResume.setEmail("karhui@student.usm.my");
        newResume.setLinkedinLink("www.linkedin.com");
        newResume.setEducation("USM Msc DSA");
        newResume.setExperience("Vitrox AI team Data Architect");
        newResume.setSkill("Python, Machine Learning, PowerBI");
        newResume.setReference("Mr. David Wong Manager of AI team Vitrox Technologies Sdn. Bhd.");
        newResume.setResumeData(mockResumeFile.getBytes());
        newResume.setImageData(mockImageFile.getBytes());

        int previousNoOfResume = resumeRepository.findAll().size();

        resumeService.saveResume(newResume);
        int currentNumOfResume = resumeRepository.findAll().size();
        assertEquals(previousNoOfResume + 1, currentNumOfResume);

    }

    @Test
    void TestUpdateResume() {
        AddResumeRequest resume = new AddResumeRequest();

        resume.setAboutMe("This is a new about me");
        resume.setReference("Mr. David Wong Senior Manager of AI team Vitrox Technologies Sdn. Bhd.");
        resume.setId(19);
        resumeService.updateResume(resume);

        var updateResume = resumeRepository.findById(resume.getId());
        assertEquals(resume.getAboutMe(), updateResume.get().getAboutMe());
        assertEquals(resume.getReference(), updateResume.get().getReference());
    }
    
}
