package com.example.ppms.controller;

import java.util.List;
import java.util.Map;
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

import com.example.ppms.jsonModel.SelectedPresentation;
import com.example.ppms.model.Presentation;
import com.example.ppms.service.PresentationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/presentation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PresentationController {

	@Autowired
	private PresentationService presentationService;

	@PutMapping("/user/{id}")
	public Map<String,Object> getPresentationsByUserId(@PathVariable("id") String id, @RequestBody String json) {
		return presentationService.getPresentationsByUserId(id, json);
	}

	@PutMapping("/host-academic/{id}")
	public Map<String,Object> getPresentationsByHostId(@PathVariable("id") String id, @RequestBody String json) {
		return presentationService.getPresentationsByHostId(id, json);
	}

	@GetMapping("/{id}")
	public Map<String,Object> getPresentations(@PathVariable("id") String id) {
		return presentationService.getPresentations(id);
 	}
	
	@GetMapping("/getAll")
	public Map<String,Object> getAllPresentations() {
		return presentationService.getAllPresentations();
	}

	@PostMapping("/create")
	public String add(@RequestBody SelectedPresentation presentation) throws Exception {
		presentationService.createPresentation(presentation);
		return "New presentation is added";
	}

	@PutMapping("/update")
	public String update(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		SelectedPresentation selected = objectMapper.readValue(json, SelectedPresentation.class);
		presentationService.updatePresentation(selected);
		return "Presentation is updated";
	}

	@GetMapping("/edit/{id}")
	public Optional<Presentation> getPresentation(@PathVariable("id") String id) {
		return presentationService.getPresentation(id);
 	}

	@DeleteMapping("/{id}")
	public String deletePresentation(@PathVariable("id") String id) {
		presentationService.deletePresentation(id);
		return "Presentation is deleted";
	}
}
