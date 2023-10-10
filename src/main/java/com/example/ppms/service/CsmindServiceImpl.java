package com.example.ppms.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.AddCsmindRequest;
import com.example.ppms.jsonModel.AddPresentationRequest;
import com.example.ppms.jsonModel.TimeSlot;
import com.example.ppms.model.Csmind;
import com.example.ppms.model.Presentation;
import com.example.ppms.model.User;
import com.example.ppms.model.Venue;
import com.example.ppms.repository.AcademicSessionRepository;
import com.example.ppms.repository.CsmindRepository;
import com.example.ppms.repository.PresentationRepository;
import com.example.ppms.repository.UserRepository;
import com.example.ppms.repository.VenueRepository;
import com.example.ppms.scheduler.SchedulerMain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CsmindServiceImpl implements CsmindService {

	@Autowired
	private CsmindRepository csmindRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ConstraintService constraintService;

	@Autowired
	private PresentationService presentationService;

	@Autowired
	private VenueService venueService;
	
	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private PresentationRepository presentationRepository;

	@Autowired
  private AcademicSessionRepository academicSessionRepository;

	@Autowired
	private UserRepository userRepository;

	private SchedulerMain schedulerMain = new SchedulerMain();

	@Override
	public void saveCsmind(AddCsmindRequest csmind, AddPresentationRequest[] presentations) throws JsonMappingException, JsonProcessingException {
		
		var academicSession = academicSessionRepository.findById(csmind.getAcademicSession());
		
		var newCsmind = new Csmind(
			csmind.getCourseCode().toUpperCase(), 
			academicSession.get(),
			csmind.getStartDate(), 
			csmind.getEndDate(), 
			csmind.getPeriodSlot(),
			csmind.getNumOfPresentations(), 
			csmind.getSchedule()
		);
		var createdCsmind = csmindRepository.save(newCsmind);
		
		//for each presentation
		for (AddPresentationRequest p : presentations) {
			var users = userService.getAllUsers();
			User student = new User();
			User supervisor = new User();
			User examiner = new User();
			User chair = new User();

			for (int i = 0; i< users.size(); i++) {
				if((users.get(i).getName()).equals(p.getName().toUpperCase())) {
						student = users.get(i);
						break;
				}
			}

			for (int i = 0; i< users.size(); i++) {
				if((users.get(i).getName()).equals(p.getSupervisor().toUpperCase())) {
						supervisor = users.get(i);
						break;
				}
			}

			for (int i = 0; i< users.size(); i++) {
				if((users.get(i).getName()).equals(p.getExaminer().toUpperCase())) {
						examiner = users.get(i);
						break;
				}
			}

			for (int i = 0; i< users.size(); i++) {
				if((users.get(i).getName()).equals(p.getChair().toUpperCase())) {
						chair = users.get(i);
						break;
				}
			}

			var presentation = new Presentation(
				("P" + Integer.toString(p.getId())), 
				p.getTitle().toUpperCase(), 
				createdCsmind, 
				student, 
				supervisor, 
				examiner, 
				chair
			);

			presentationService.savePresentation(presentation);
		}

		//for each venue
		ObjectMapper mapper = new ObjectMapper();
		String[] venues = mapper.readValue(csmind.getRooms(), String[].class);
		for (var v : venues) {
			var venue = new Venue(v, createdCsmind);
			venueService.saveVenue(venue);
		}

	}

	public void updateCsmind(AddCsmindRequest csmind) {

		var academicSession = academicSessionRepository.findById(csmind.getAcademicSession());

		csmindRepository.findById(csmind.getId()).ifPresent(csmind1 -> {
			csmind1.setAcademicSession(academicSession.get());
			csmind1.setCourseCode(csmind.getCourseCode().toUpperCase());
			csmind1.setStartDate(csmind.getStartDate());
			csmind1.setEndDate(csmind.getEndDate());
			csmind1.setPeriodSlot(csmind.getPeriodSlot());
			csmind1.setNumOfPresentations(csmind.getNumOfPresentations());
			csmindRepository.save(csmind1);
		});
	}

	public Optional<Csmind> getCsmind(String id) {
		Integer csmindId = Integer.parseInt(id);
		return csmindRepository.findById(csmindId);
	}

	@Override
	public List<Csmind> getAllCsminds() {
		return csmindRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
	}
	
	public void generateSchedule(String id) throws IOException {

		List<File> files = constraintService.getConstraintFolder(id);

		Integer csmindId = Integer.parseInt(id);

		File solutionFile = schedulerMain.run(files);

		List<String> presentationSlots = new ArrayList<String>();
		Scanner sc = new Scanner(solutionFile);  
		sc.useDelimiter(",");
		while (sc.hasNext())
		{ 
			presentationSlots.add(String.valueOf(sc.next()));
		}
		sc.close();
		
		var slotPerDay = 15;
		List<TimeSlot> timeSlots = new ArrayList<>();

		for (int ps = 0; ps < presentationSlots.size(); ps++) {
			if(!presentationSlots.get(ps).equals("null")) {
				int slotNum = ps + 1;
				int column = slotNum - ((slotNum / slotPerDay) * slotPerDay);
				int row = (slotNum / slotPerDay) + 1;
				String header = "P" + presentationSlots.get(ps);
				timeSlots.add(new TimeSlot(row, column, true, header));
			}
		}

		var venues = venueRepository.findAllByCsmindId(csmindId);
		var presentations = presentationRepository.findAllByCsmindId(csmindId);

		ObjectMapper mapper = new ObjectMapper();
		TimeSlot[] table = mapper.readValue(venues.get(0).getUnavailableTime(), TimeSlot[].class);
		String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		var numOfDays = table[table.length-1].getRow();
		var csmind = csmindRepository.findById(csmindId);
		String startDay = new SimpleDateFormat("EEEE").format(csmind.get().getStartDate());
		int startIndex = Arrays.asList(DAYS).indexOf(startDay);
		int endIndex = startIndex;
		for (int i = 0; i < numOfDays-1; i++) {
			endIndex = endIndex + 1;
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		
		//find venue and time to update each presentation
		for (int t = 0; t < timeSlots.size(); t++) {

			if( (timeSlots.get(t).getRow() % venues.size()) == 0) {
				var presIdentity = timeSlots.get(t).getHeader();
				var roomAbbr = "R" + String.valueOf(venues.size());
				var room = venues.stream().filter(item -> item.getAbbr().equals(roomAbbr)).findFirst().get().getName();
				var day = DAYS[(timeSlots.get(t).getRow() / venues.size()) - 1];
				var time = table[timeSlots.get(t).getColumn()].getHeader();
				var temp = presentations.stream().filter(item -> item.getPresIdentity().equals(presIdentity)).findFirst();
				
				var startDate = csmind.get().getStartDate();
				var i = (timeSlots.get(t).getRow() / venues.size()) - 1;
				String date = startDate.toLocalDate().plusDays(i-startIndex).format(dateTimeFormatter);

				presentationRepository.findById(temp.get().getId()).ifPresent(pres1 -> {
					pres1.setDay(day);
					pres1.setDate(date);
					pres1.setTimeSlot(time);
					pres1.setVenue(room);
					presentationRepository.save(pres1);
				});
			}
			else {
				var presIdentity = timeSlots.get(t).getHeader();
				var roomAbbr = "R" + String.valueOf( (timeSlots.get(t).getRow() % venues.size()) );
				var room = venues.stream().filter(item -> item.getAbbr().equals(roomAbbr)).findFirst().get().getName();
				var day = DAYS[(timeSlots.get(t).getRow() / venues.size())];
				var time = table[timeSlots.get(t).getColumn()].getHeader();
				var temp = presentations.stream().filter(item -> item.getPresIdentity().equals(presIdentity)).findFirst();
				
				var startDate = csmind.get().getStartDate();
				var i = timeSlots.get(t).getRow() / venues.size();
				String date = startDate.toLocalDate().plusDays(i-startIndex).format(dateTimeFormatter);

				presentationRepository.findById(temp.get().getId()).ifPresent(pres1 -> {
					pres1.setDay(day);
					pres1.setDate(date);
					pres1.setTimeSlot(time);
					pres1.setVenue(room);
					presentationRepository.save(pres1);
				});
			}

		}

		//Generate master schedule and save to database
		List<TimeSlot> masterSchedule = new ArrayList<>();
		presentations = presentationRepository.findAllByCsmindId(csmindId);
		for (int d = startIndex; d <= endIndex; d++) {
			for (int i = 0; i <= venues.size(); i++) {
				for (int j = 0; j < slotPerDay+1; j++) {
					
					if (i==0 && j==0) {
						masterSchedule.add(new TimeSlot(i, j, false, ""));
					}
					else if (i==0) {
						masterSchedule.add(new TimeSlot(i, j, false, table[j].getHeader()));
					}
					else if (j==0) {
						masterSchedule.add(new TimeSlot(i, j, false, venues.get(i-1).getName()));
					}
					else {
						var header = table[j].getHeader();
						var venue = venues.get(i-1).getName();
						var day = DAYS[d];
						var temp = presentations.stream().filter(item -> item.getTimeSlot().equals(header) && item.getVenue().equals(venue) && item.getDay().equals(day)).findFirst();
						if (!temp.isEmpty()) {
							masterSchedule.add(new TimeSlot(i, j, true, temp.get().getPresIdentity()));
						}
						else {
							masterSchedule.add(new TimeSlot(i, j, false, ""));
						}
					}
	
				}
			}
		}

		var schedule = mapper.writeValueAsString(masterSchedule);
		csmindRepository.findById(csmindId).ifPresent(csmind1 -> {
			csmind1.setSchedule(schedule);
			csmindRepository.save(csmind1);
		});
	}

	public List<Csmind> getUserCsminds(String id) {
		Integer userId = Integer.parseInt(id);

		List<Integer> csmindIds = new ArrayList<>();
		var presentations = presentationRepository.findAll();
		for (Presentation p : presentations) {
			if (p.getStudent().getId() == userId || p.getSupervisor().getId() == userId || p.getExaminerOne().getId() == userId || p.getExaminerTwo().getId() == userId) {
				csmindIds.add(p.getCsmind().getId());
			}
		}
		
		List<Integer> csmindIdsNoDuplicate = csmindIds.stream().distinct().collect(Collectors.toList());
		
		return csmindRepository.findAllByIdInOrderByStartDateDesc(csmindIdsNoDuplicate);
	}
	
	public List<User> getChairSelection(String id) {
		Integer csmindId = Integer.parseInt(id);
		List<Presentation> presentations = presentationRepository.findAllByCsmindId(csmindId);

		List<Integer> chairIds = new ArrayList<>();
		for (Presentation p : presentations) {
			chairIds.add(p.getExaminerTwo().getId());
		}
		
		List<Integer> chairIdsNoDuplicate = chairIds.stream().distinct().collect(Collectors.toList());
		var chairs = userRepository.findAllByIdIn(chairIdsNoDuplicate);

		return chairs;
	}

	public void updateSchedule(String id, Csmind csmind) throws JsonMappingException, JsonProcessingException {
		Integer csmindId = Integer.parseInt(id);
		var retrievedCsmind = csmindRepository.findById(csmindId);

		ObjectMapper mapper = new ObjectMapper();
		TimeSlot[] timeSlots = mapper.readValue(csmind.getSchedule(), TimeSlot[].class);
		
		var presentations = presentationRepository.findAllByCsmindId(csmindId);
		//update each presentation
		for (Presentation presentation : presentations) {
			for (TimeSlot timeSlot : timeSlots) {
				if (presentation.getPresIdentity().equals(timeSlot.getHeader())) {
					presentationRepository.findById(presentation.getId()).ifPresent(pres1 -> {
						String venueName = timeSlots[timeSlot.getRow()*16].getHeader();
						pres1.setVenue(venueName);
						presentationRepository.save(pres1);
					});
					break;
				}
			}
		}

		csmindRepository.findById(retrievedCsmind.get().getId()).ifPresent(csmind1 -> {
			csmind1.setSchedule(csmind.getSchedule());
			csmindRepository.save(csmind1);
		});
	}
}
