package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.jsonModel.AddConstraintRequest;
import com.example.ppms.repository.ConstraintRepository;
import com.example.ppms.service.ConstraintService;

@SpringBootTest
public class ConstraintServiceImplTests {
  
  @Autowired
  private ConstraintRepository constraintRepository;

  @Autowired
  private ConstraintService constraintService;

  @Test
  void TestCreateConstraint() throws Exception {
    
    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":false,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Thursday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    AddConstraintRequest constraint = new AddConstraintRequest();
    constraint.setCsmindId(2);
    constraint.setUserId(30);
    constraint.setUnavailableTime(unavailableTime);
    constraint.setPreferVenueChange(true);
    constraint.setNumOfConsecutiveSlots(2);
    constraint.setRole("lecturer");

    // Get previous number of constraints 
    int previousNumOfConstraints = constraintRepository.findAll().size();

    // Run unit testing
    constraintService.saveConstraint(constraint);
    int currentNumOfConstraints = constraintRepository.findAll().size();
    assertEquals(previousNumOfConstraints+1, currentNumOfConstraints);
  }

  @Test
  void TestCannotCreateConstraint() {
    
    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":false,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Thursday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    AddConstraintRequest constraint = new AddConstraintRequest();
    constraint.setCsmindId(1);
    constraint.setUserId(30);
    constraint.setUnavailableTime(unavailableTime);
    constraint.setPreferVenueChange(true);
    constraint.setNumOfConsecutiveSlots(2);
    constraint.setRole("lecturer");

    // Run unit testing
    Exception exception = assertThrows(Exception.class, () -> 
      constraintService.saveConstraint(constraint)
    );
    assertEquals("Constraint is already exists.", exception.getMessage());
  }

  @Test
  void TestUpdateConstraint() {

    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":false,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Thursday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    AddConstraintRequest constraint = new AddConstraintRequest();
    constraint.setId(2);
    constraint.setUnavailableTime(unavailableTime);
    constraint.setPreferVenueChange(true);
    constraint.setNumOfConsecutiveSlots(2);
    
    // Run unit testing
    constraintService.updateConstraint(constraint);
    var updatedConstraint = constraintRepository.findById(constraint.getId());
    assertEquals(constraint.getUnavailableTime(), updatedConstraint.get().getUnavailableTime());
    assertEquals(constraint.isPreferVenueChange(), updatedConstraint.get().isPreferVenueChange());
    assertEquals(constraint.getNumOfConsecutiveSlots(), updatedConstraint.get().getNumOfConsecutiveSlots());
  }

  @Test
  void TestCannotUpdateConstraint() {

    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":false,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Thursday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    AddConstraintRequest constraint = new AddConstraintRequest();
    constraint.setId(1000);
    constraint.setUnavailableTime(unavailableTime);
    constraint.setPreferVenueChange(true);
    constraint.setNumOfConsecutiveSlots(2);
    
    // Run unit testing
    constraintService.updateConstraint(constraint);
    var updatedConstraint = constraintRepository.findById(constraint.getId());
    assertTrue(updatedConstraint.isEmpty());
  }

  @Test
  void TestDeleteConstraint() {

    // Create Test Input
    var allConstraints = constraintRepository.findAll();
    var lastCreatedConstraint = allConstraints.get(allConstraints.size()-1);
    String constraintId =  Integer.toString(lastCreatedConstraint.getId());

    // Run unit testing
    constraintService.deleteConstraint(constraintId);
    var retrievedConstraint = constraintRepository.findById(lastCreatedConstraint.getId());
    assertTrue(retrievedConstraint.isEmpty());
  }

}
