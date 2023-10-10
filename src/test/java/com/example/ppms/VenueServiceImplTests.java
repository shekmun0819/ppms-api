package com.example.ppms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ppms.model.Venue;
import com.example.ppms.repository.VenueRepository;
import com.example.ppms.service.VenueService;

@SpringBootTest
public class VenueServiceImplTests {
  
  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private VenueService venueService;

  @Test
  void TestUpdateVenue() {

    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":true,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Monday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    Venue venue = new Venue();
    venue.setId(2);
    venue.setUnavailableTime(unavailableTime);
    
    // Run unit testing
    venueService.updateVenue(venue);
    var updatedVenue = venueRepository.findById(venue.getId());
    assertEquals(venue.getUnavailableTime(), updatedVenue.get().getUnavailableTime());
  }

  @Test
  void TestCannotUpdateVenue() {

    // Default unavailable time
    String unavailableTime = "[{\"row\":0,\"column\":0,\"flag\":false,\"header\":\"\"},{\"row\":0,\"column\":1,\"flag\":false,\"header\":\"08:30-09:00\"},{\"row\":0,\"column\":2,\"flag\":false,\"header\":\"09:00-09:30\"},{\"row\":0,\"column\":3,\"flag\":false,\"header\":\"09:30-10:00\"},{\"row\":0,\"column\":4,\"flag\":false,\"header\":\"10:00-10:30\"},{\"row\":0,\"column\":5,\"flag\":false,\"header\":\"10:30-11:00\"},{\"row\":0,\"column\":6,\"flag\":false,\"header\":\"11:00-11:30\"},{\"row\":0,\"column\":7,\"flag\":false,\"header\":\"11:30-12:00\"},{\"row\":0,\"column\":8,\"flag\":false,\"header\":\"12:00-12:30\"},{\"row\":0,\"column\":9,\"flag\":false,\"header\":\"14:00-14:30\"},{\"row\":0,\"column\":10,\"flag\":false,\"header\":\"14:30-15:00\"},{\"row\":0,\"column\":11,\"flag\":false,\"header\":\"15:00-15:30\"},{\"row\":0,\"column\":12,\"flag\":false,\"header\":\"15:30-16:00\"},{\"row\":0,\"column\":13,\"flag\":false,\"header\":\"16:00-16:30\"},{\"row\":0,\"column\":14,\"flag\":false,\"header\":\"16:30-17:00\"},{\"row\":0,\"column\":15,\"flag\":true,\"header\":\"17:00-x\"},{\"row\":1,\"column\":0,\"flag\":false,\"header\":\"Monday\"},{\"row\":1,\"column\":1,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":2,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":3,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":4,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":5,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":6,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":7,\"flag\":false,\"header\":\"\"},{\"row\":1,\"column\":8,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":9,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":10,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":11,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":12,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":13,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":14,\"flag\":true,\"header\":\"\"},{\"row\":1,\"column\":15,\"flag\":true,\"header\":\"\"}]";

    // Create Test Input
    Venue venue = new Venue();
    venue.setId(1000);
    venue.setUnavailableTime(unavailableTime);

    // Run unit testing
    venueService.updateVenue(venue);
    var updatedVenue = venueRepository.findById(venue.getId());
    assertTrue(updatedVenue.isEmpty());
  }

  @Test
  void TestDeleteVenue() {

    // Create Test Input
    var allVenues = venueRepository.findAll();
    var lastCreatedVenue = allVenues.get(allVenues.size()-1);
    String venueId =  Integer.toString(lastCreatedVenue.getId());

    // Run unit testing
    venueService.deleteVenue(venueId);
    var retrievedVenue = venueRepository.findById(lastCreatedVenue.getId());
    assertTrue(retrievedVenue.isEmpty());
  }

}
