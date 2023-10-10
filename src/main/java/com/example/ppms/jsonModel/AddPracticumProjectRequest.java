package com.example.ppms.jsonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPracticumProjectRequest {

    private int id;
    private String name;
    private String description;
    private String status;
    private String adminDescription;
    private String imageName;
    private String ndaName;
    private boolean isNDA;
    private String courseName;
    private String courseCode;
    private String categories;

    private byte[] imageData;
    private byte[] ndaData;

    private int academicSession;
    private int host;
    
}
