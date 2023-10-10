package com.example.ppms.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "AcademicSessions")
public class AcademicSession extends BaseEntity {

    @Id
    @Column(name = "academic_session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String academicSession;
    private int semester;
    private Date startDate;
    private Date endDate;
    private boolean active;

    public AcademicSession() {
    }
}
