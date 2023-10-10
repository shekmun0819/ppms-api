package com.example.ppms.jsonModel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
  
  private int row;
  private int column;
  private boolean flag;
  private String header;
}
