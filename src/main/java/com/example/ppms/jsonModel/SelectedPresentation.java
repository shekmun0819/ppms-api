package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectedPresentation {
  
  private int id;
  private String title;
  private int student;
  private int supervisor;
  private int examiner;
  private int chair;
}
