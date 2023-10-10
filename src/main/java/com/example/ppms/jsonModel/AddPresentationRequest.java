package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPresentationRequest {
  
  private int id;
	private String matricNo;
	private String name;
	private String title;
	private String supervisor;
	private String examiner;
	private String chair;
}