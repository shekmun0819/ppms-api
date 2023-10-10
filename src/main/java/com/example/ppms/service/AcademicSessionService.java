package com.example.ppms.service;

import java.util.List;
import java.util.Optional;

import com.example.ppms.model.AcademicSession;

public interface AcademicSessionService {
    public AcademicSession saveAcademicSession(AcademicSession academicSession);

    public List<AcademicSession> getAllAcademicSessions();

    public void updateAcademicSessions(AcademicSession academicSession);

    public Optional<AcademicSession> getAcademicSession(String id);

    public List<AcademicSession> getHostAcademics(String id);
}
