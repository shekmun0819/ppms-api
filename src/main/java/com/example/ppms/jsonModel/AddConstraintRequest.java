package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddConstraintRequest {

  private int id;
  private String role;
  private int csmindId;
  private int userId;
  private int numOfConsecutiveSlots;
	private boolean preferVenueChange;
  private String unavailableTime;
}
