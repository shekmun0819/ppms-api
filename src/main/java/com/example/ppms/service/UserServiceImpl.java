package com.example.ppms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.UserSelection;
import com.example.ppms.jwt.AuthenticationService;
import com.example.ppms.jwt.RegisterRequest;
import com.example.ppms.model.Host;
import com.example.ppms.model.Lecturer;
import com.example.ppms.model.Student;
import com.example.ppms.model.User;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.CompanyRepository;
import com.example.ppms.repository.HostRepository;
import com.example.ppms.repository.LecturerRepository;
import com.example.ppms.repository.StudentRepository;
import com.example.ppms.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private LecturerRepository lecturerRepository;

	@Autowired
	private HostRepository hostRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private AuthenticationService service;

	@Autowired
  private AcademicSessionRepository academicSessionRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void importUser(RegisterRequest[] users) {
		var companies = companyRepository.findAll();

		for (RegisterRequest user : users) {
			
			if (user.getRoles().contains("Host")) {
				var company =  companies.stream().filter(item -> item.getCompanyName().equals(user.getCompanyName().toUpperCase())).findFirst();
				if (company.isPresent()) {
					user.setCompanyId(company.get().getId());
				}
			}
			
			service.register(user);
		}
	}

	public void updateUser(RegisterRequest user) {

		var existingUser = userRepository.findById(user.getId());

		userRepository.findById(user.getId()).ifPresent(user1 -> {
			user1.setRoles(user.getRoles());
			user1.setEmail(user.getEmail());
			user1.setName(user.getName());
			userRepository.save(user1);
		});

		if(user.getRoles().contains("Student")) {
			var student = studentRepository.findAllByUserId(user.getId());
			var academicSession = academicSessionRepository.findByAcademicSessionAndSemester(user.getAcademicSession(), user.getSemester());

			if(student.isPresent()) {
				studentRepository.findAllByUserId(user.getId()).ifPresent(student1 -> {
					student1.setMatricNum(user.getMatricNum());
					student1.setCourseCode(user.getCourseCode());
					student1.setAcademicSession(academicSession.get());
					studentRepository.save(student1);
				});
			}
			else {
				Student newStudent = new Student(existingUser.get(), user.getMatricNum(), user.getCourseCode(), academicSession.get());
				studentRepository.save(newStudent);
			}
		}

		if(user.getRoles().contains("Lecturer")) {
			if(user.getRoles().contains("Admin")) {
				var lecturer = lecturerRepository.findAllByUserId(user.getId());
				if(lecturer.isPresent()) {
					lecturerRepository.findAllByUserId(user.getId()).ifPresent(lecturer1 -> {
						lecturer1.setStaffId(user.getStaffId());
						lecturer1.setProgrammeManager(true);
						lecturerRepository.save(lecturer1);
					});
				}
				else {
					var newLecturer = new Lecturer(existingUser.get(), user.getStaffId(), true);
					lecturerRepository.save(newLecturer);
				}
			}
			else {
				var lecturer = lecturerRepository.findAllByUserId(user.getId());
				if(lecturer.isPresent()) {
					lecturerRepository.findAllByUserId(user.getId()).ifPresent(lecturer1 -> {
						lecturer1.setStaffId(user.getStaffId());
						lecturer1.setProgrammeManager(false);
						lecturerRepository.save(lecturer1);
					});
				}
				else {
					var newLecturer = new Lecturer(existingUser.get(), user.getStaffId(), false);
					lecturerRepository.save(newLecturer);
				}
			}
		}

		if(user.getRoles().contains("Host")) {
			var host = hostRepository.findAllByUserId(user.getId());
			var company = companyRepository.findById(user.getCompanyId());

			if(host.isPresent()) {
				hostRepository.findAllByUserId(user.getId()).ifPresent(host1 -> {
					host1.setCompany(company.get());
					hostRepository.save(host1);
				});
			}
			else {
				var newHost = new Host(existingUser.get(), company.get());
				hostRepository.save(newHost);
			}
		}
	
	}

	public void activateUser(List<String> userIds) {

		for (String userId : userIds) {
			userRepository.findById(Integer.parseInt(userId)).ifPresent(user1 -> {
				user1.setActive(true);
				userRepository.save(user1);
			});
		}

	}

	public void deactivateUser(List<String> userIds) {

		for (String userId : userIds) {
			userRepository.findById(Integer.parseInt(userId)).ifPresent(user1 -> {
				user1.setActive(false);
				userRepository.save(user1);
			});
		}

	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
	}

	//to get personal information of user
	public Map<String,Object> getUser(String id) {
		Map<String,Object> map = new HashMap<>();
		
		Integer userId = Integer.parseInt(id);
		map.put("user", userRepository.findById(userId));
		map.put("student", studentRepository.findAllByUserId(userId));
		map.put("lecturer", lecturerRepository.findAllByUserId(userId));
		map.put("host", hostRepository.findAllByUserId(userId));

		return map;
	}
	
	//to get users selection
	public Map<String,Object> getUserSelection() { 
		Map<String,Object> map = new HashMap<>();
		List<UserSelection> studentSelection = new ArrayList<UserSelection>();
		List<UserSelection> lecturerSelection = new ArrayList<UserSelection>();
		List<UserSelection> hostSelection = new ArrayList<UserSelection>();

		List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.ASC, "user.name"));
		for (Student student : students) {
			studentSelection.add(new UserSelection(student.getUser().getId(), student.getUser().getName()));
		}

		List<Lecturer> lecturers = lecturerRepository.findAll(Sort.by(Sort.Direction.ASC, "user.name"));
		for (Lecturer lecturer : lecturers) {
			lecturerSelection.add(new UserSelection(lecturer.getUser().getId(), lecturer.getUser().getName()));
		}

		List<Host> hosts = hostRepository.findAll(Sort.by(Sort.Direction.ASC, "user.name"));
		for (Host host : hosts) {
			hostSelection.add(new UserSelection(host.getUser().getId(), host.getUser().getName()));
		}
		
		map.put("student", studentSelection);
		map.put("lecturer", lecturerSelection);
		map.put("host", hostSelection);

		return map;
	}

	@Override
	public User getUserByEmailAndPassword(String email, String password) {
		User user = userRepository.findByEmailAndPassword(email, password);
		if(user == null) {
			//throw new UserNotFoundException("Invalid email and password");
			System.out.println("Invalid email and password");
			
		}
		return user;
	}

	@Override
	public User getUserbyEmailAndPassword(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
