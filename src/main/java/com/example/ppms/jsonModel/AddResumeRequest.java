package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddResumeRequest {
  
  private int id;
  private int student;
  private String filename;
  private String imageName;
  private String name;
  private String contact;
  private String email;
  private String linkedinLink;
  private String aboutMe;
  private String education;
  private String experience;
  private String skill;
  private String reference;
  private byte[] resumeData;
  private byte[] imageData;
}