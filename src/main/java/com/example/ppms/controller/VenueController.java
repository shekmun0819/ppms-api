package com.example.ppms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.jsonModel.AddVenueRequest;
import com.example.ppms.model.Venue;
import com.example.ppms.service.VenueService;

@RestController
@RequestMapping("api/v1/venue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VenueController {

	@Autowired
	private VenueService venueService;

	@GetMapping("/{id}")
	List<Venue> getVenues(@PathVariable("id") String id) {
		return venueService.getVenues(id);
 	}

	@PostMapping("/add")
	public String add(@RequestBody AddVenueRequest venue) {
		venueService.addVenue(venue);
		return "New venue is added";
	}

	@DeleteMapping("/{id}")
	public String deleteVenue(@PathVariable("id") String id) {
		venueService.deleteVenue(id);
		return "Venue is deleted";
 	}
	
	@GetMapping("/getAll")
	public List<Venue> getAllVenues() {
		return venueService.getAllVenues();
	}

	@GetMapping("/edit/{id}")
	Optional<Venue> getVenue(@PathVariable("id") String id) {
		return venueService.getVenue(id);
 	}

	@PutMapping("/update")
	public String update(@RequestBody Venue venue) {
	 venueService.updateVenue(venue);
	 return "Venue is updated";
	}
}
