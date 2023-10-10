package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.ppms.jsonModel.AddPracticumProjectRequest;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.service.PracticumProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.sql.Date;

@SpringBootTest
public class PracticumProjectServiceImplTests {

    @Autowired
    private PracticumProjectService practicumProjectService;

    @Autowired
    private PracticumProjectRepository practicumProjectRepository;

    @Test
    void TestUpdateProjectAsssessor() {
        // Create test input
        int projectId = 1;
        int supervisorId = 2;
        int examinerId = 3;
        int panelId = 4;
        int chairId = 5;

        // Run unit testing
        practicumProjectService.updateProjectAssessor(projectId, supervisorId, examinerId, panelId, chairId);
        var updatedProject = practicumProjectRepository.findById(projectId);
        assertEquals(supervisorId, updatedProject.get().getSupervisor().getId());
        assertEquals(examinerId, updatedProject.get().getExaminer().getId());
        assertEquals(panelId, updatedProject.get().getPanel().getId());
        assertEquals(chairId, updatedProject.get().getChair().getId());
    }

    @Test
    void TestUpdateProjectAsssessorToNotDetermined() {
        // Create test input
        int projectId = 1;
        int supervisorId = 0;
        int examinerId = 0;
        int panelId = 0;
        int chairId = 0;

        // Run unit testing
        practicumProjectService.updateProjectAssessor(projectId, supervisorId, examinerId, panelId, chairId);
        var updatedProject = practicumProjectRepository.findById(projectId);
        assertEquals(null, updatedProject.get().getSupervisor());
        assertEquals(null, updatedProject.get().getExaminer());
        assertEquals(null, updatedProject.get().getPanel());
        assertEquals(null, updatedProject.get().getChair());
    }

    @Test
    void TestSavePracticumProjet() throws JsonMappingException, JsonProcessingException, IOException {

        final MultipartFile mockImageFile = mock(MultipartFile.class);
        when(mockImageFile.getOriginalFilename()).thenReturn("image.jpg");

        final MultipartFile mockNdaFile = mock(MultipartFile.class);
        when(mockNdaFile.getOriginalFilename()).thenReturn("nda_company.pdf");

        AddPracticumProjectRequest newPracticumProject = new AddPracticumProjectRequest();
        // newPracticumProject.setId(26);
        newPracticumProject.setAcademicSession(1);
        newPracticumProject.setAdminDescription("This project is stricty confidential.");
        newPracticumProject.setCourseCode("CDS590");
        newPracticumProject.setCourseName("Data Science and Analytics");
        newPracticumProject.setDescription("This is a project description");
        newPracticumProject.setCategories("Education");
        newPracticumProject.setHost(109);
        newPracticumProject.setImageData(mockImageFile.getBytes());
        newPracticumProject.setImageName(mockImageFile.getOriginalFilename());
        newPracticumProject.setNdaName(mockNdaFile.getOriginalFilename());
        newPracticumProject.setNdaData(mockNdaFile.getBytes());
        newPracticumProject.setNDA(true);
        newPracticumProject.setName("ACCELERATED LIFE TEST FOR RFID INLAYs");
        newPracticumProject.setStatus("Open");

        int previousNumOfPracticumProject = practicumProjectRepository.findAll().size();
        practicumProjectService.savePracticumProject(newPracticumProject);

        int currentNumOfPracticumProject = practicumProjectRepository.findAll().size();
        assertEquals(previousNumOfPracticumProject + 1, currentNumOfPracticumProject);
    }

    @Test
    void TestCannotCreatePracticumProject() throws IOException {

        final MultipartFile mockImageFile = mock(MultipartFile.class);
        when(mockImageFile.getOriginalFilename()).thenReturn("image.jpg");

        final MultipartFile mockNdaFile = mock(MultipartFile.class);
        when(mockNdaFile.getOriginalFilename()).thenReturn("nda_company.pdf");

        AddPracticumProjectRequest newPracticumProject = new AddPracticumProjectRequest();
        newPracticumProject.setAcademicSession(1);
        newPracticumProject.setAdminDescription("This project is stricty confidential.");
        newPracticumProject.setCourseCode("CDS590");
        newPracticumProject.setCourseName("Data Science and Analytics");
        newPracticumProject.setDescription("This is a project description");
        newPracticumProject.setCategories("Education");
        newPracticumProject.setHost(109);
        newPracticumProject.setImageData(mockImageFile.getBytes());
        newPracticumProject.setImageName(mockImageFile.getOriginalFilename());
        newPracticumProject.setNdaName(mockNdaFile.getOriginalFilename());
        newPracticumProject.setNdaData(mockNdaFile.getBytes());
        newPracticumProject.setNDA(true);
        newPracticumProject.setName("ACCELERATED LIFE TEST FOR RFID INLAY");
        newPracticumProject.setStatus("Open");

        Exception exception = assertThrows(Exception.class,
                () -> practicumProjectService.savePracticumProject(newPracticumProject));
        assertEquals("could not execute statement; SQL [n/a]; constraint [UK_eix4pve37e0td4qrnudwyq5vo]", exception.getMessage());
    }

    @Test 
    void TestUpdatePracticumProject() {
        AddPracticumProjectRequest practicumProject = new AddPracticumProjectRequest();
        practicumProject.setId(23);
        practicumProject.setCategories("Environment");
        practicumProject.setDescription("This is a new project description");

        practicumProjectService.updatePracticumProject(practicumProject);

        var updatePracticumProject = practicumProjectRepository.findById(practicumProject.getId());
        assertEquals(practicumProject.getCategories(), updatePracticumProject.get().getCategories());
        assertEquals(practicumProject.getDescription(), updatePracticumProject.get().getDescription());

    }

    @Test
    void TestDeactivatePracticumProject() {

        AddPracticumProjectRequest practicumProject = new AddPracticumProjectRequest();
        practicumProject.setId(23);
        practicumProject.setStatus("Closed");

        practicumProjectService.deactivatePracticumProject(practicumProject);

        var deactivatePracticumProject = practicumProjectRepository.findById(practicumProject.getId());
        assertEquals(practicumProject.getStatus(), deactivatePracticumProject.get().getStatus());
    }

}
