package com.example.ppms.service;

import java.util.List;
import java.util.Optional;

import com.example.ppms.jsonModel.AddVenueRequest;
import com.example.ppms.model.Venue;

public interface VenueService {

	public void saveVenue(Venue venue);
	public void addVenue(AddVenueRequest venue);
	public void deleteVenue(String id);
	public List<Venue> getVenues(String id);
	public Optional<Venue> getVenue(String id);
	public void updateVenue(Venue venue);
	public List<Venue> getAllVenues();
}
