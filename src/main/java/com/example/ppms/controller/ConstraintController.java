package com.example.ppms.controller;

import java.io.IOException;
import java.nio.file.Path;
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

import com.example.ppms.jsonModel.AddConstraintRequest;
import com.example.ppms.model.Constraint;
import com.example.ppms.service.ConstraintService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("api/v1/constraint")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConstraintController {

	@Autowired
	private ConstraintService constraintService;

	@PostMapping("/create")
	public String add(@RequestBody AddConstraintRequest constraint) throws Exception {
		constraintService.saveConstraint(constraint);
		return "New constraint is added";
	}
	
	@GetMapping("/{id}")
	Optional<Constraint> getConstraint(@PathVariable("id") String id) {
		return constraintService.getConstraint(id);
 	}

	@PutMapping("/update")
	 public String update(@RequestBody AddConstraintRequest constraint) {
		constraintService.updateConstraint(constraint);
		return "Constraint is updated";
	 }

	@DeleteMapping("/{id}")
	public String deleteConstraint(@PathVariable("id") String id) {
		constraintService.deleteConstraint(id);
		return "Constraint is deleted";
 	}

	@GetMapping("/getAll")
	public Map<String,Object> getAllConstraints() {
		return constraintService.getAllConstraints();
	}

	@GetMapping("/getUserConstraints/{id}")
	public List<Constraint> getUserConstraints(@PathVariable("id") String id) {
		return constraintService.getUserConstraints(id);
	}

	@GetMapping("/checkConstraints/{id}")
	public Boolean checkConstraints(@PathVariable("id") String id) throws JsonMappingException, JsonProcessingException {
		Boolean valid = constraintService.checkConstraints(id);
		return valid;
	} 
}
