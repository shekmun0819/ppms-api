package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.jsonModel.SelectedPresentation;
import com.example.ppms.repository.PresentationRepository;
import com.example.ppms.service.PresentationService;

@SpringBootTest
public class PresentationServiceImplTests {
  
  @Autowired
  private PresentationService presentationService;

  @Autowired
	private PresentationRepository presentationRepository;

  @Test
  void TestCreatePresentation() {
    
    // Create Test Input
    SelectedPresentation newPresentation = new SelectedPresentation();
    newPresentation.setId(1);
    newPresentation.setTitle("Test Create Presentation");
    newPresentation.setStudent(50);
    newPresentation.setSupervisor(4);
    newPresentation.setExaminer(5);
    newPresentation.setChair(6);

    // Get previous number of presentations for this csmind
    int previousNumOfPresentations = presentationRepository.findAllByCsmindId(newPresentation.getId()).size();

    // Run unit testing
    presentationService.createPresentation(newPresentation);
    int currentNumOfPresentations = presentationRepository.findAllByCsmindId(newPresentation.getId()).size();
    assertEquals(previousNumOfPresentations+1, currentNumOfPresentations);
  }

  @Test
  void TestCannotCreatePresentation() {
    
    // Create Test Input
    SelectedPresentation newPresentation = new SelectedPresentation();
    newPresentation.setId(1000);
    newPresentation.setTitle("Test Create Presentation");
    newPresentation.setStudent(50);
    newPresentation.setSupervisor(4);
    newPresentation.setExaminer(5);
    newPresentation.setChair(6);

    // Run unit testing
    Exception exception = assertThrows(Exception.class, () -> 
      presentationService.createPresentation(newPresentation)
    );
    assertEquals(NoSuchElementException.class, exception.getClass());
  }

  @Test
  void TestUpdatePresentation() {

    // Create Test Input
    SelectedPresentation selectedPresentation = new SelectedPresentation();
    selectedPresentation.setId(2);
    var presentation = presentationRepository.findById(selectedPresentation.getId());
    selectedPresentation.setStudent(presentation.get().getStudent().getId());
    selectedPresentation.setSupervisor(presentation.get().getSupervisor().getId());
    selectedPresentation.setExaminer(presentation.get().getExaminerOne().getId());
    selectedPresentation.setChair(4);
    selectedPresentation.setTitle(presentation.get().getTitle());

    // Run unit testing
    presentationService.updatePresentation(selectedPresentation);
    var retrievedPresentation = presentationRepository.findById(selectedPresentation.getId());
    assertEquals(selectedPresentation.getChair(), retrievedPresentation.get().getExaminerTwo().getId());
  }

  @Test
  void TestCannotUpdatePresentation() {

    // Create Test Input
    SelectedPresentation selectedPresentation = new SelectedPresentation();
    selectedPresentation.setId(1000);
    selectedPresentation.setStudent(50);
    selectedPresentation.setSupervisor(4);
    selectedPresentation.setExaminer(5);
    selectedPresentation.setChair(4);
    selectedPresentation.setTitle("PREDICTING CUSTOMER CHURN FOR RETAIL DEPARTMENT STORE USING MACHINE LEARNING");

    // Run unit testing
    presentationService.updatePresentation(selectedPresentation);
    var retrievedPresentation = presentationRepository.findById(selectedPresentation.getId());
    assertTrue(retrievedPresentation.isEmpty());
  }

  @Test
  void TestDeletePresentation() {

    // Create Test Input
    var allPresentations = presentationRepository.findAll();
    var lastCreatedPresentation = allPresentations.get(allPresentations.size()-1);
    String presentationId =  Integer.toString(lastCreatedPresentation.getId());

    // Run unit testing
    presentationService.deletePresentation(presentationId);
    var retrievedPresentation = presentationRepository.findById(lastCreatedPresentation.getId());
    assertTrue(retrievedPresentation.isEmpty());
  }
}
