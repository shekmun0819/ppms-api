package com.example.ppms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.ppms.model.PracticumProject;
import com.fasterxml.jackson.databind.node.BooleanNode;

@Repository
public interface PracticumProjectRepository extends JpaRepository<PracticumProject, Integer> {

	public List<PracticumProject> findAllByHostId(Integer userid);

	public PracticumProject findAllByStudentId(Integer id);

	public PracticumProject findByStudentId(Integer userId);

	public List<Object> findAllBySupervisorIdAndStudentIsNotNull(int supId);

	public List<Object> findAllByExaminerIdAndStudentIsNotNull(int exaId);

	public List<Object> findAllByPanelIdAndStudentIsNotNull(int panelId);

	public List<Object> findAllByHostIdAndStudentIsNotNull(int hostId);

	public List<PracticumProject> findAllByStatusAndAcademicSession_active(String status, Boolean active);

	public List<PracticumProject> findAllByAcademicSessionId(int acadId);

	public List<PracticumProject> findAllByHostIdAndAcademicSessionIdAndStudentIsNotNull(int hostId, int acadId);

	public List<PracticumProject> findAllByAcademicSessionIdAndStudentIsNotNull(int acadId);
}
