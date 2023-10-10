package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.jsonModel.AddCsmindRequest;
import com.example.ppms.jsonModel.AddPresentationRequest;
import com.example.ppms.model.Csmind;
import com.example.ppms.repository.CsmindRepository;
import com.example.ppms.repository.PresentationRepository;
import com.example.ppms.repository.VenueRepository;
import com.example.ppms.service.CsmindService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class CsmindServiceImplTests {

  @Autowired
	private CsmindService csmindService;

  @Autowired
  private CsmindRepository csmindRepository;

  @Autowired
	private PresentationRepository presentationRepository;

  @Autowired
	private VenueRepository venuerRepository;
  
  @Test
	void TestGetAllCsminds() {
    List<Csmind> csminds = csmindService.getAllCsminds();
    assertEquals(2, csminds.size());
	}

  @Test
  void TestUpdateCsmind() {

    // Create Test Input
    AddCsmindRequest csmind = new AddCsmindRequest();
    csmind.setAcademicSession(1);
    csmind.setId(1);
    csmind.setCourseCode("CDT594");

    // Run unit testing
    csmindService.updateCsmind(csmind);
    var retrievedCsmind = csmindRepository.findById(csmind.getId());
    assertEquals("CDT594", retrievedCsmind.get().getCourseCode());
  }

  @Test
  void TestSaveCsmind() throws JsonMappingException, JsonProcessingException {
    long millis=System.currentTimeMillis();  
    Date date = new Date(millis);

    // Create Test Input
    AddCsmindRequest newCsmind = new AddCsmindRequest();
    newCsmind.setAcademicSession(1);
    newCsmind.setCourseCode("CDS590");
    newCsmind.setStartDate(date);
    newCsmind.setEndDate(date);
    newCsmind.setPeriodSlot("30-Minute per slot");
    newCsmind.setNumOfPresentations(1);

    String[] rooms = {"DKG31", "SCL1"};
    ObjectMapper mapper = new ObjectMapper();
    var stringRooms = mapper.writeValueAsString(rooms);
    newCsmind.setRooms(stringRooms);

    List<AddPresentationRequest> newPresentations = new ArrayList<AddPresentationRequest>();
    AddPresentationRequest presentationOne = new AddPresentationRequest();
    presentationOne.setId(1);
    presentationOne.setName("AMIR SYUKRI BIN SHAHAR");
    presentationOne.setSupervisor("NASUHA LEE ABDULLAH, Dr.");
    presentationOne.setExaminer("AZIZUL RAHMAN MOHD SHARIFF, Dr.");
    presentationOne.setChair("NASUHA LEE ABDULLAH, Dr.");
    presentationOne.setTitle("INTERACTIVE PERFORMANCE DASHBOARD WITH MODULE POWER PREDICTION FOR PRINTED CIRCUIT BOARD ASSEMBLY");
    newPresentations.add(presentationOne);
    AddPresentationRequest[] array = new AddPresentationRequest[newPresentations.size()];
    newPresentations.toArray(array);

    // Get previous number of records
    int previousNumOfCsmind = csmindRepository.findAll().size();
    int previousNumOfPresentation = presentationRepository.findAll().size();
    int previousNumOfVenue = venuerRepository.findAll().size();
    
    // Run unit testing
    csmindService.saveCsmind(newCsmind, array);

    int currentNumOfCsmind = csmindRepository.findAll().size();
    assertEquals(previousNumOfCsmind+1, currentNumOfCsmind);

    int currentNumOfPresentation = presentationRepository.findAll().size();
    assertEquals(previousNumOfPresentation+1, currentNumOfPresentation);

    int currentNumOfVenue = venuerRepository.findAll().size();
    assertEquals(previousNumOfVenue+2, currentNumOfVenue);
  }

}
