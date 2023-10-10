package com.example.ppms.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.ppms.jsonModel.AddConstraintRequest;
import com.example.ppms.model.Constraint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ConstraintService {

	public void saveConstraint(AddConstraintRequest constraint) throws Exception;
	public Optional<Constraint> getConstraint(String id);
	public void updateConstraint(AddConstraintRequest constraint);
	public void deleteConstraint(String id);
	public Map<String,Object> getAllConstraints();
	public List<Constraint> getUserConstraints(String id);
	public List<File> getConstraintFolder(String id) throws IOException;
	public Boolean checkConstraints(String id) throws JsonMappingException, JsonProcessingException;
}
