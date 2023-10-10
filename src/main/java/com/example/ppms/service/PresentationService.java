package com.example.ppms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.ppms.jsonModel.SelectedPresentation;
import com.example.ppms.model.Presentation;

public interface PresentationService {

	public void savePresentation(Presentation presentation);
	public void createPresentation(SelectedPresentation presentation);
	public void updatePresentation(SelectedPresentation selected);
	public void deletePresentation(String id);
	public Map<String,Object> getPresentations(String id);
	public Map<String,Object> getPresentationsByUserId(String id, String json);
	public Map<String,Object> getPresentationsByHostId(String id, String json);
	public Map<String,Object> getAllPresentations();
	public Optional<Presentation> getPresentation(String id);
}
