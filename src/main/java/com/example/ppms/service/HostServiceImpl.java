package com.example.ppms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ppms.model.Host;
import com.example.ppms.repository.HostRepository;

@Service
public class HostServiceImpl implements HostService {

	@Autowired
	private HostRepository hostRepository;

	@Override
	public List<Host> getAllHosts() {
		return hostRepository.findAll();
	}
	
}
