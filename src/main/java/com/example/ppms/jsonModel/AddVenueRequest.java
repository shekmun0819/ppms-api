package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddVenueRequest {
  
  private int csmindId;
  private String room;
}
