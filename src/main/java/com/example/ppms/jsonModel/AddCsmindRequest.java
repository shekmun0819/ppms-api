package com.example.ppms.jsonModel;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCsmindRequest {
  
  private int id;
	private String courseCode;
	private int academicSession;
	private Date startDate;
	private Date endDate;
	private String periodSlot;
	private int numOfRooms;
	private int numOfPresentations;
	private String schedule;
  private String data;
	private String rooms;
}
