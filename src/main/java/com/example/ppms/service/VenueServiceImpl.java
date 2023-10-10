package com.example.ppms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.AddVenueRequest;
import com.example.ppms.model.Venue;
import com.example.ppms.repository.CsmindRepository;
import com.example.ppms.repository.VenueRepository;

@Service
public class VenueServiceImpl implements VenueService {

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private CsmindRepository csmindRepository;

	@Override
	public void saveVenue(Venue venue) {
		venueRepository.save(venue);
	}

	public void addVenue(AddVenueRequest venue) {
		var csmind = csmindRepository.findById(venue.getCsmindId());
		var newVenue = new Venue(venue.getRoom(), csmind.get());
		venueRepository.save(newVenue);
	}

	public void deleteVenue(String id) {
		Integer venueId = Integer.parseInt(id);
		venueRepository.deleteById(venueId);
	}

	public List<Venue> getVenues(String id) {
		Integer csmindId = Integer.parseInt(id);
		return venueRepository.findAllByCsmindId(csmindId);
	}

	@Override
	public List<Venue> getAllVenues() {
		return venueRepository.findAll(Sort.by(Sort.Direction.DESC, "csmind.startDate", "name"));
	}
	
	public Optional<Venue> getVenue(String id) {
		Integer venueId = Integer.parseInt(id);
		return venueRepository.findById(venueId);
	}

	public void updateVenue(Venue venue) {
		venueRepository.findById(venue.getId()).ifPresent(venue1 -> {
			venue1.setUnavailableTime(venue.getUnavailableTime());
			venueRepository.save(venue1);
		});
	}
}
