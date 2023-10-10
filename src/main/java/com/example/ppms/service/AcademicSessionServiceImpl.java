package com.example.ppms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.model.AcademicSession;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.PracticumProjectRepository;

@Service
public class AcademicSessionServiceImpl implements AcademicSessionService {

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private PracticumProjectRepository practicumProjectRepository;

    @Override
    public AcademicSession saveAcademicSession(AcademicSession academicSession) {
        academicSessionRepository.findByActiveTrue().ifPresent(acadSession -> {
            acadSession.setActive(false);
            academicSessionRepository.save(acadSession);
        });

        return academicSessionRepository.save(academicSession);
    }

    @Override
    public List<AcademicSession> getAllAcademicSessions() {

        return academicSessionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void updateAcademicSessions(AcademicSession academicSession) {
        academicSessionRepository.findById(academicSession.getId()).ifPresent(academicSession1 -> {
            // academicSession1.setAcademicSession(academicSession.getAcademicSession());
            // academicSession1.setSemester(academicSession.getSemester());
            academicSession1.setStartDate(academicSession.getStartDate());
            academicSession1.setEndDate(academicSession.getEndDate());
            // academicSession1.setActive(academicSession.isActive());
            academicSessionRepository.save(academicSession1);
        });
    }

    @Override
    public Optional<AcademicSession> getAcademicSession(String id) {
        Integer academicSessionId = Integer.parseInt(id);
        return academicSessionRepository.findById(academicSessionId);
    }

    public List<AcademicSession> getHostAcademics(String id) {
        Integer userId = Integer.parseInt(id);

		List<Integer> academicIds = new ArrayList<>();
		var projects = practicumProjectRepository.findAll();
		for (PracticumProject project : projects) {
			if (project.getHost().getId() == userId) {
				academicIds.add(project.getAcademicSession().getId());
			}
		}
		
		List<Integer> academicIdsNoDuplicate = academicIds.stream().distinct().collect(Collectors.toList());
		
		return academicSessionRepository.findAllByIdInOrderByCreatedAtDesc(academicIdsNoDuplicate);
	}
}
