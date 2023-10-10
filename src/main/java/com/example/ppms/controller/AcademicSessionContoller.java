package com.example.ppms.controller;

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

import com.example.ppms.model.AcademicSession;
import com.example.ppms.service.AcademicSessionService;

@RestController
@RequestMapping("api/v1/academic-session")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AcademicSessionContoller {

    @Autowired
    private AcademicSessionService academicSessionService;

    @PostMapping("/create")
    public String add(@RequestBody AcademicSession academicSession) {
        academicSessionService.saveAcademicSession(academicSession);
        return "New academic session is added";
    }

    @GetMapping("/getAll")
    public List<AcademicSession> getAllAcademicSessions() {
        return academicSessionService.getAllAcademicSessions();
    }

    @PutMapping("/update")
    public String updateAcademicSession(@RequestBody AcademicSession academicSession) {
        academicSessionService.updateAcademicSessions(academicSession);
        return "Academic session is updated";
    }

    @GetMapping("/{id}")
    Optional<AcademicSession> getAcademicSession(@PathVariable("id") String id) {
        return academicSessionService.getAcademicSession(id);
    }

    @GetMapping("/getHostAcademics/{id}")
	public List<AcademicSession> getHostAcademics(@PathVariable("id") String id) {
		return academicSessionService.getHostAcademics(id);
	}
}
