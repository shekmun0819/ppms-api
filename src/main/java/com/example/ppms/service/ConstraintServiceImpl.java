package com.example.ppms.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ppms.jsonModel.AddConstraintRequest;
import com.example.ppms.jsonModel.TimeSlot;
import com.example.ppms.model.Constraint;
import com.example.ppms.model.Presentation;
import com.example.ppms.model.Venue;
import com.example.ppms.repository.ConstraintRepository;
import com.example.ppms.repository.CsmindRepository;
import com.example.ppms.repository.PresentationRepository;
import com.example.ppms.repository.UserRepository;
import com.example.ppms.repository.VenueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConstraintServiceImpl implements ConstraintService {

	@Autowired
	private ConstraintRepository constraintRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CsmindRepository csmindRepository;

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private PresentationRepository presentationRepository;

	@Override
	public void saveConstraint(AddConstraintRequest constraint) throws Exception {

		var existConstraint = constraintRepository.findByCsmindIdAndUserId(constraint.getCsmindId(), constraint.getUserId());

		if (existConstraint.isEmpty()) {
			var csmind = csmindRepository.findById(constraint.getCsmindId());
			var user = userRepository.findById(constraint.getUserId());
			var newConstraint = new Constraint();
			if (constraint.getRole().contains("lecturer")) {
				newConstraint = new Constraint(
				constraint.getUnavailableTime(),
				constraint.getNumOfConsecutiveSlots(),
				constraint.isPreferVenueChange(),
				csmind.get(),
				user.get());
			} 
			else {
				newConstraint = new Constraint(
				constraint.getUnavailableTime(),
				csmind.get(),
				user.get());
			}
		
			constraintRepository.save(newConstraint);
		}

		else {
			throw new Exception("Constraint is already exists.");
		}
	}

	public Optional<Constraint> getConstraint(String id) {
		Integer constraintId = Integer.parseInt(id);
		return constraintRepository.findById(constraintId);
	}

	public void updateConstraint(AddConstraintRequest constraint) {
		constraintRepository.findById(constraint.getId()).ifPresent(constraint1 -> {
			constraint1.setNumOfConsecutiveSlots(constraint.getNumOfConsecutiveSlots());
			constraint1.setPreferVenueChange(constraint.isPreferVenueChange());
			constraint1.setUnavailableTime(constraint.getUnavailableTime());
			constraintRepository.save(constraint1);
		});
	}

	public void deleteConstraint(String id) {
		Integer constraintId = Integer.parseInt(id);
		constraintRepository.deleteById(constraintId);
	}

	@Override
	public Map<String,Object> getAllConstraints() {
		Map<String,Object> map = new HashMap<>();
		List<Constraint> allConstraints = constraintRepository.findAll(Sort.by(Sort.Direction.DESC, "csmind.startDate", "user.name"));
		List<Constraint> lectConstraints = new ArrayList<Constraint>();
		List<Constraint> stuConstraints = new ArrayList<Constraint>();

		for (Constraint constraint : allConstraints) {
			if(constraint.getUser().getRoles().contains("Lecturer")) {
				lectConstraints.add(constraint);
			}
			else {
				stuConstraints.add(constraint);
			}
		}
		
		map.put("lecturer", lectConstraints);
		map.put("student", stuConstraints);

		return map;
	}
	
	public List<File> getConstraintFolder(String id) throws IOException {
	
		var slotPerDay = 15;
		String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		List<String> daysList = Arrays.asList(DAYS);

		Integer csmindId = Integer.parseInt(id);
		var csmind = csmindRepository.findById(csmindId);

		String timeStamp = new SimpleDateFormat("(ddMMMyyyy)").format(csmind.get().getStartDate());
		File directory = new File(FileSystemView.getFileSystemView().getHomeDirectory() + "/Constraints" + timeStamp);
    if (!directory.exists()) {
			directory.mkdirs();
		}

		//------------------------------HC03------------------------------------------
		var venues = venueRepository.findAllByCsmindId(csmindId);
		File venueFile = new File(directory + "\\HC03.csv");
		FileOutputStream venueFos = new FileOutputStream(venueFile);
		BufferedWriter venueBw = new BufferedWriter(new OutputStreamWriter(venueFos));

		venueBw.write("r=" + venues.size());
		venueBw.newLine();
		venueBw.write("Room #,unavailable time slots, ...");
		venueBw.newLine();

		for (int v = 0; v < venues.size(); v++) {

			ObjectMapper mapper = new ObjectMapper();
			TimeSlot[] timeSlots = mapper.readValue(venues.get(v).getUnavailableTime(), TimeSlot[].class);
			int dayIndex = -1;
			List<Integer> unavailableSlots = new ArrayList<Integer>();
			List<Integer> dayUsed = new ArrayList<Integer>();

			for (int i = 0; i < timeSlots.length; i++) {

				if (timeSlots[i].getRow() == 0) {
				}
				
				else if (daysList.contains(timeSlots[i].getHeader())) {
					dayIndex = daysList.indexOf(timeSlots[i].getHeader());
					dayUsed.add(dayIndex);
				}
				
				else if (timeSlots[i].isFlag()) {
					var position = (venues.size() * slotPerDay * dayIndex) + timeSlots[i].getColumn() + (slotPerDay * v);
					unavailableSlots.add(position);
				}
				
				else {
				}
			}

			for (int i = 0; i < daysList.size(); i++) {
				if (!dayUsed.contains(i)) {
					int startPosition = (venues.size() * slotPerDay * i) + 1 + ( v * slotPerDay);
					int endPosition = (venues.size() * slotPerDay * i) + slotPerDay + ( v * slotPerDay);
					for (int j = startPosition; j <= endPosition; j++) {
						unavailableSlots.add(j);
					}
				}
			}

			String venueUnavailable = unavailableSlots.toString();
			venueUnavailable = venueUnavailable.replace("[", "").replace("]", "").replace(" ", "");
			
			String roomAbbr = "R" + String.valueOf(v+1);
			venueRepository.findById(venues.get(v).getId()).ifPresent(venue1 -> {
				venue1.setAbbr(roomAbbr);
				venueRepository.save(venue1);
			});
			
			venueBw.write(roomAbbr);
			venueBw.write(",");
			venueBw.write(venueUnavailable);
			venueBw.newLine();
		}

    venueBw.close();
		venueFos.close();
		//----------------------------------------------------------------------------

		var constraints = constraintRepository.findAllByCsmindId(csmindId);
		List<Constraint> lectConstraints = new ArrayList<Constraint>();
		List<Constraint> stuConstraints = new ArrayList<Constraint>();
		for (Constraint constraint : constraints) {
			if(constraint.getUser().getRoles().contains("Lecturer")) {
				lectConstraints.add(constraint);
			}
			else {
				stuConstraints.add(constraint);
			}
		}

		//------------------------------HC04------------------------------------------
		File lectConstFile = new File(directory + "\\HC04.csv");
		FileOutputStream lectConstFos = new FileOutputStream(lectConstFile);
		BufferedWriter lectConstBw = new BufferedWriter(new OutputStreamWriter(lectConstFos));

		lectConstBw.write("n=" + lectConstraints.size());
		lectConstBw.newLine();
		lectConstBw.write("Staff #,unavailable time slots, ...");
		lectConstBw.newLine();

		//------------------------------SC01------------------------------------------
		File consecutiveFile = new File(directory + "\\SC01.csv");
		FileOutputStream consecutiveFos = new FileOutputStream(consecutiveFile);
		BufferedWriter consecutiveBw = new BufferedWriter(new OutputStreamWriter(consecutiveFos));

		consecutiveBw.write("n=" + lectConstraints.size() + ",");
		consecutiveBw.newLine();
		consecutiveBw.write("Staff #,maximum consecutive slot");
		consecutiveBw.newLine();

		//------------------------------SC02------------------------------------------
		File maxDayFile = new File(directory + "\\SC02.csv");
		FileOutputStream maxDayFos = new FileOutputStream(maxDayFile);
		BufferedWriter maxDayBw = new BufferedWriter(new OutputStreamWriter(maxDayFos));

		maxDayBw.write("n=" + lectConstraints.size() + ",");
		maxDayBw.newLine();
		maxDayBw.write("Staff #,maximum number of days");
		maxDayBw.newLine();

		//------------------------------SC03------------------------------------------
		File venueChangeFile = new File(directory + "\\SC03.csv");
		FileOutputStream venueChangeFos = new FileOutputStream(venueChangeFile);
		BufferedWriter venueChangeBw = new BufferedWriter(new OutputStreamWriter(venueChangeFos));

		venueChangeBw.write("n=" + lectConstraints.size() + ",");
		venueChangeBw.newLine();
		venueChangeBw.write("Staff #,change venue of not");
		venueChangeBw.newLine();

		for (int c = 0; c < lectConstraints.size(); c++) {

			ObjectMapper mapper = new ObjectMapper();
			TimeSlot[] timeSlots = mapper.readValue(lectConstraints.get(c).getUnavailableTime(), TimeSlot[].class);
			int dayIndex = -1;
			List<Integer> unavailableSlots = new ArrayList<Integer>();

			for (int i = 0; i < timeSlots.length; i++) {

				if (timeSlots[i].getRow() == 0) {
				}
				
				else if (daysList.contains(timeSlots[i].getHeader())) {
					dayIndex = daysList.indexOf(timeSlots[i].getHeader());
				}
				
				else if (timeSlots[i].isFlag()) {
					for (int v = 0; v < venues.size(); v++) {
						var position = (venues.size() * slotPerDay * dayIndex) + timeSlots[i].getColumn() + (slotPerDay * v);
						unavailableSlots.add(position);	
					}
				}
				
				else {
				}
			}

			String lectUnavailable = unavailableSlots.toString();
			lectUnavailable = lectUnavailable.replace("[", "").replace("]", "").replace(" ", "");

			String lectAbbr = "S" + String.format("%03d", (c+1)); //leadingZero
			constraintRepository.findById(lectConstraints.get(c).getId()).ifPresent(constraint1 -> {
				constraint1.setAbbr(lectAbbr);
				constraintRepository.save(constraint1);
			});

			lectConstBw.write(lectAbbr);
			lectConstBw.write(",");
			lectConstBw.write(lectUnavailable);
			lectConstBw.newLine();

			consecutiveBw.write(lectAbbr);
			consecutiveBw.write(",");
			consecutiveBw.write(String.valueOf(lectConstraints.get(c).getNumOfConsecutiveSlots()));
			consecutiveBw.newLine();

			maxDayBw.write(lectAbbr);
			maxDayBw.write(",");
			maxDayBw.write(String.valueOf(timeSlots[timeSlots.length-1].getRow()));
			maxDayBw.newLine();

			venueChangeBw.write(lectAbbr);
			venueChangeBw.write(",");
			venueChangeBw.write(lectConstraints.get(c).isPreferVenueChange() ? "yes" : "no");
			venueChangeBw.newLine();
		}

    lectConstBw.close();
		lectConstBw.close();

    consecutiveBw.close();
		consecutiveBw.close();

		maxDayBw.close();
		maxDayBw.close();

		venueChangeBw.close();
		venueChangeBw.close();
		//----------------------------------------------------------------------------

		//--------------------------SupExaAssign--------------------------------------
		constraints = constraintRepository.findAllByCsmindId(csmindId);
		var presentations = presentationRepository.findAllByCsmindId(csmindId);

		File assignFile = new File(directory + "\\SupExaAssign.csv");
		FileOutputStream assignFos = new FileOutputStream(assignFile);
		BufferedWriter assignBw = new BufferedWriter(new OutputStreamWriter(assignFos));

		assignBw.write("m=" + presentations.size());
		assignBw.newLine();
		assignBw.write("Presentation #,Supervisor,Examiner 1,Examiner 2");
		assignBw.newLine();

		for (int p = 0; p < presentations.size(); p++) {
			assignBw.write(presentations.get(p).getPresIdentity());
			assignBw.write(",");

			var supervisorId = presentations.get(p).getSupervisor().getId();
			var temp = constraints.stream().filter(item -> item.getUser().getId() == supervisorId).findFirst();
			assignBw.write(String.valueOf(temp.get().getAbbr()));
			assignBw.write(",");

			var examinerId = presentations.get(p).getExaminerOne().getId();
			temp = constraints.stream().filter(item -> item.getUser().getId() == examinerId).findFirst();
			assignBw.write(String.valueOf(temp.get().getAbbr()));
			assignBw.write(",");

			var chairId = presentations.get(p).getExaminerTwo().getId();
			temp = constraints.stream().filter(item -> item.getUser().getId() == chairId).findFirst();
			assignBw.write(String.valueOf(temp.get().getAbbr()));
			assignBw.newLine();
		}

		assignBw.close();
		assignBw.close();
		//----------------------------------------------------------------------------

		//------------------------------HC05------------------------------------------
		File stuConstFile = new File(directory + "\\HC05.csv");
		FileOutputStream stuConstFos = new FileOutputStream(stuConstFile);
		BufferedWriter stuConstBw = new BufferedWriter(new OutputStreamWriter(stuConstFos));

		stuConstBw.write("m=" + presentations.size());
		stuConstBw.newLine();
		stuConstBw.write("Presentation #,unavailable time slots, ...");
		stuConstBw.newLine();

		for (int p = 0; p < presentations.size(); p++) {
			stuConstBw.write(presentations.get(p).getPresIdentity());
			
			var studentId = presentations.get(p).getStudent().getId();
			var tempStuConst = stuConstraints.stream().filter(item -> item.getUser().getId() == studentId).findFirst();
			
			if(!tempStuConst.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				TimeSlot[] timeSlots = mapper.readValue(tempStuConst.get().getUnavailableTime(), TimeSlot[].class);
				int dayIndex = -1;
				List<Integer> unavailableSlots = new ArrayList<Integer>();

				for (int i = 0; i < timeSlots.length; i++) {

					if (timeSlots[i].getRow() == 0) {
					}
					
					else if (daysList.contains(timeSlots[i].getHeader())) {
						dayIndex = daysList.indexOf(timeSlots[i].getHeader());
					}
					
					else if (timeSlots[i].isFlag()) {
						for (int v = 0; v < venues.size(); v++) {
							var position = (venues.size() * slotPerDay * dayIndex) + timeSlots[i].getColumn() + (slotPerDay * v);
							unavailableSlots.add(position);	
						}
					}
					
					else {
					}
				}

				String stuUnavailable = unavailableSlots.toString();
				stuUnavailable = stuUnavailable.replace("[", "").replace("]", "").replace(" ", "");

				stuConstBw.write(",");
				stuConstBw.write(stuUnavailable);
			}

			stuConstBw.newLine();
		}

		stuConstBw.close();
		stuConstBw.close();

		List<File> files = new ArrayList<File>();
		files.add(venueFile);
		files.add(lectConstFile);
		files.add(stuConstFile);
		files.add(consecutiveFile);
		files.add(maxDayFile);
		files.add(venueChangeFile);
		files.add(assignFile);

		return files;
	}

	public Boolean checkConstraints(String id) throws JsonMappingException, JsonProcessingException {
		Integer csmindId = Integer.parseInt(id);
		var csmind = csmindRepository.findById(csmindId);

		var venues = venueRepository.findAllByCsmindId(csmindId);
		if (venues.size() < 4) {
			return false;
		}

		for (Venue venue : venues) {
			if (venue.getUnavailableTime() == null) {
				return false;
			}
		}

		var constraints = constraintRepository.findAllByCsmindId(csmindId);
		var presentations = presentationRepository.findAllByCsmindId(csmindId);

		//get all users involved in that csmind
		List<Integer> involvedUserIds = new ArrayList<>();
		for (Presentation p : presentations) {
			involvedUserIds.add(p.getSupervisor().getId());
			involvedUserIds.add(p.getExaminerOne().getId());
			involvedUserIds.add(p.getExaminerTwo().getId());
		}
		List<Integer> involvedUserIdsNoDuplicate = involvedUserIds.stream().distinct().collect(Collectors.toList());

		//Create default constraint
		ObjectMapper mapper = new ObjectMapper();
		TimeSlot[] timeSlots = mapper.readValue(venues.get(0).getUnavailableTime(), TimeSlot[].class);
		String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		var numOfDays = timeSlots[timeSlots.length-1].getRow();
		String startDay = new SimpleDateFormat("EEEE").format(csmind.get().getStartDate());
		int startIndex = Arrays.asList(DAYS).indexOf(startDay);
		int endIndex = startIndex;
		for (int i = 0; i < numOfDays-1; i++) {
			endIndex = endIndex + 1;
		}

		var slotPerDay = 15;
		List<TimeSlot> defaultUnavailable = new ArrayList<>();
		List<Integer> defaultFlags = new ArrayList<>();
		for (int i = 0; i < numOfDays+1; i++) {
			for (int j = 0; j < slotPerDay+1; j++) {
				if (i==0 && j==0) {
					defaultUnavailable.add(new TimeSlot(i, j, false, ""));
				}
				else if (i==0) {
					defaultUnavailable.add(new TimeSlot(i, j, false, timeSlots[j].getHeader()));
					if (timeSlots[j].getHeader().contains("x")) {
						defaultFlags.add(j);
					}
				}
				else if (j==0) {
					defaultUnavailable.add(new TimeSlot(i, j, false, DAYS[startIndex+i-1]));
				}
				else if (defaultFlags.contains(j)) {
					defaultUnavailable.add(new TimeSlot(i, j, true, "" ));
				}
				else {
					defaultUnavailable.add(new TimeSlot(i, j, false, "" ));
				}
				
			}
		}
	
		var unavailableTime = mapper.writeValueAsString(defaultUnavailable);

		for (Integer involvedUserId : involvedUserIdsNoDuplicate) {
			Optional<Constraint> constraint = constraints.stream().filter(item -> item.getUser().getId() == involvedUserId).findFirst();
			if ( !constraint.isPresent() ) {
				var user = userRepository.findById(involvedUserId);
				var newConstraint = new Constraint(unavailableTime, 4, false, csmind.get(), user.get());
				constraintRepository.save(newConstraint);
			}
		}

		return true;
	}

	public List<Constraint> getUserConstraints(String id) {
		Integer userId = Integer.parseInt(id);
		return constraintRepository.findAllByUserIdOrderByCsmindStartDateDesc(userId);
	}
}
