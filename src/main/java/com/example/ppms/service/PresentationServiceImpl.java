package com.example.ppms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.SelectedPresentation;
import com.example.ppms.model.PracticumProject;
import com.example.ppms.model.Presentation;
import com.example.ppms.model.Student;
import com.example.ppms.repository.CsmindRepository;
import com.example.ppms.repository.PracticumProjectRepository;
import com.example.ppms.repository.PresentationRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;

@Service
public class PresentationServiceImpl implements PresentationService {

	@Autowired
	private PresentationRepository presentationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CsmindRepository csmindRepository;

	@Autowired
	private PracticumProjectRepository practicumProjectRepository;

	@Override
	public void savePresentation(Presentation presentation) {
		presentationRepository.save(presentation);
	}

	public void createPresentation(SelectedPresentation presentation) {
		var student = userRepository.findById(presentation.getStudent());
		var supervisor = userRepository.findById(presentation.getSupervisor());
		var examiner = userRepository.findById(presentation.getExaminer());
		var chair = userRepository.findById(presentation.getChair());
		var csmind = csmindRepository.findById(presentation.getId());

		var csmindPresentation = presentationRepository.findAllByCsmindId(presentation.getId());
		String temp = "P" + Integer.toString(csmindPresentation.size() + 1);

		var pres = new Presentation(
			temp,
			presentation.getTitle(), 
			csmind.get(), 
			student.get(), 
			supervisor.get(), 
			examiner.get(), 
			chair.get()
		);

		savePresentation(pres);

		//Update total number of presentations for that particular csmind
		csmindRepository.findById(csmind.get().getId()).ifPresent(csmind1 -> {
			csmind1.setNumOfPresentations(presentationRepository.findAllByCsmindId(csmind.get().getId()).size());
			csmindRepository.save(csmind1);
		});
	}

	public void updatePresentation(SelectedPresentation selected) {
		var student = userRepository.findById(selected.getStudent());
		var supervisor = userRepository.findById(selected.getSupervisor());
		var examiner = userRepository.findById(selected.getExaminer());
		var chair = userRepository.findById(selected.getChair());

		presentationRepository.findById(selected.getId()).ifPresent(pres -> {
			pres.setTitle(selected.getTitle());
			pres.setStudent(student.get());
			pres.setSupervisor(supervisor.get());
			pres.setExaminerOne(examiner.get());
			pres.setExaminerTwo(chair.get());
			presentationRepository.save(pres);
		});

	}
	
	public void deletePresentation(String id) {
		Integer presentationId = Integer.parseInt(id);
		
		var deletedPres = presentationRepository.findById(presentationId);
		presentationRepository.deleteById(presentationId);

		var presentations = presentationRepository.findAllByCsmindId(deletedPres.get().getCsmind().getId());

		for (int i = 0; i < presentations.size(); i++) {
			
			String temp = "P" + Integer.toString(i+1);

			presentationRepository.findById(presentations.get(i).getId()).ifPresent(pres -> {
				pres.setPresIdentity(temp);
				presentationRepository.save(pres);
			});
		}

		//Update total number of presentations for that particular csmind
		csmindRepository.findById(deletedPres.get().getCsmind().getId()).ifPresent(csmind1 -> {
			csmind1.setNumOfPresentations(presentationRepository.findAllByCsmindId(deletedPres.get().getCsmind().getId()).size());
			csmindRepository.save(csmind1);
		});
	}

	public Map<String,Object> getPresentations(String id) {
		Map<String,Object> map = new HashMap<>();

		Integer csmindId = Integer.parseInt(id);
		List<Presentation> presentations = presentationRepository.findAllByCsmindId(csmindId);
		List<Student> students = new ArrayList<>();

		for (Presentation p : presentations) {
			students.add(studentRepository.findAllByUserId(p.getStudent().getId()).get());
		}

		map.put("presentations", presentations);
		map.put("students", students);

		return map;
	}

	public Map<String,Object> getPresentationsByUserId(String id, String json) {
		Map<String,Object> map = new HashMap<>();
		var temp = json.split(":");
		temp[1] = temp[1].replace("{", "").replace("}", "").replace(" ", "").replace("\"", "");

		Integer csmindId = Integer.parseInt(id);
		Integer userId = Integer.parseInt(temp[1]);

		var presentations = presentationRepository.findAllByCsmindId(csmindId);
		List<Presentation> pByUser = new ArrayList<>();
		List<Student> students = new ArrayList<>();
		
		for (Presentation p : presentations) {
			if (p.getStudent().getId() == userId || p.getSupervisor().getId() == userId || p.getExaminerOne().getId() == userId || p.getExaminerTwo().getId() == userId) {
				pByUser.add(p);
				students.add(studentRepository.findAllByUserId(p.getStudent().getId()).get());
			}
		}

		map.put("presentations", pByUser);
		map.put("students", students);

		return map;
	}

	public Map<String,Object> getPresentationsByHostId(String id, String json) {
		Map<String,Object> map = new HashMap<>();
		var temp = json.split(":");
		temp[1] = temp[1].replace("{", "").replace("}", "").replace(" ", "").replace("\"", "");

		Integer acadId = Integer.parseInt(id);
		Integer hostId = Integer.parseInt(temp[1]);
		
		List<Integer> studentIds = new ArrayList<>();
		var projects = practicumProjectRepository.findAllByHostIdAndAcademicSessionIdAndStudentIsNotNull(hostId, acadId);
		for (PracticumProject project : projects) {
			studentIds.add(project.getStudent().getId());
		}
		List<Integer> studentIdsNoDuplicate = studentIds.stream().distinct().collect(Collectors.toList());
		
		var presentations = presentationRepository.findAllByStudentIdInOrderByStudentNameAsc(studentIdsNoDuplicate);
		List<Student> students = new ArrayList<>();
		
		for (Presentation p : presentations) {
			students.add(studentRepository.findAllByUserId(p.getStudent().getId()).get());
		}

		map.put("presentations", presentations);
		map.put("students", students);

		return map;
	}

	@Override
	public Map<String,Object> getAllPresentations() {
		Map<String,Object> map = new HashMap<>();

		var presentations = presentationRepository.findAll(Sort.by("csmind.startDate").descending().and(Sort.by("student.name")));
		List<Student> students = new ArrayList<>();
		for (Presentation p : presentations) {
			students.add(studentRepository.findAllByUserId(p.getStudent().getId()).get());
		}

		map.put("presentations", presentations);
		map.put("students", students);

		return map;
	}
	
	public Optional<Presentation> getPresentation(String id) {
		Integer presentationId = Integer.parseInt(id);
		return presentationRepository.findById(presentationId);
	}
}
