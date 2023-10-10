package com.example.ppms.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ppms.jsonModel.AddCsmindRequest;
import com.example.ppms.jsonModel.AddPresentationRequest;
import com.example.ppms.model.Csmind;
import com.example.ppms.model.User;
import com.example.ppms.service.CsmindService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/v1/csmind")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CsmindController {

	@Autowired
	private CsmindService csmindService;
	
	@PostMapping("/create")
	public String add(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		AddCsmindRequest csmind = objectMapper.readValue(json, AddCsmindRequest.class);
		AddPresentationRequest[] presentations = objectMapper.readValue(csmind.getData(), AddPresentationRequest[].class);
		csmindService.saveCsmind(csmind, presentations);
		return "New CSMInD is created";
	}

	@GetMapping("/{id}")
	Optional<Csmind> getCsmind(@PathVariable("id") String id) {
		return csmindService.getCsmind(id);
 	}

	@PutMapping("/update")
	 public String update(@RequestBody AddCsmindRequest csmind) {
		csmindService.updateCsmind(csmind);
		return "CSMInD is updated";
	}

	@GetMapping("/getAll")
	public List<Csmind> getAllCsminds() {
		return csmindService.getAllCsminds();
	}

	@GetMapping("/getUserCsminds/{id}")
	public List<Csmind> getUserCsminds(@PathVariable("id") String id) {
		return csmindService.getUserCsminds(id);
	}

	@GetMapping("/getChairSelection/{id}")
	public List<User> getChairSelection(@PathVariable("id") String id) {
		return csmindService.getChairSelection(id);
	}

	@PutMapping("/generateSchedule/{id}")
	public String generateSchedule(@PathVariable("id") String id) throws IOException {
		csmindService.generateSchedule(id);
		return "Master Schedule is generated.";
	}

	@PutMapping("/updateSchedule/{id}")
	public void updateSchedule(@PathVariable("id") String id, @RequestBody Csmind csmind) throws JsonMappingException, JsonProcessingException {
		csmindService.updateSchedule(id, csmind);
	}
}
