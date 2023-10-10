package com.example.ppms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.ppms.jsonModel.AddCsmindRequest;
import com.example.ppms.jsonModel.AddPresentationRequest;
import com.example.ppms.model.Csmind;
import com.example.ppms.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface CsmindService {

	public void saveCsmind(AddCsmindRequest csmind, AddPresentationRequest[] presentations) throws JsonMappingException, JsonProcessingException;
	public void updateCsmind(AddCsmindRequest csmind);
	public Optional<Csmind> getCsmind(String id);
	public List<Csmind> getAllCsminds();
	public List<Csmind> getUserCsminds(String id);
	public void generateSchedule(String id) throws IOException;
	public void updateSchedule(String id, Csmind csmind) throws JsonMappingException, JsonProcessingException;
	public List<User> getChairSelection(String id);
}
