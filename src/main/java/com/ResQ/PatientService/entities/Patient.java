package com.resq.PatientService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient {
    //primary properties
    @Id
    private Integer national_Id;
    private String first_name;
    private String last_name;
    private Integer age;
    private String gender;
    private String address;
    private String contact_number;
    private Integer ward_number;
    private boolean isDeleted = false;
    //other properties
    private String criticality;
    private boolean admission_status;
    private String assigned_doctor;
    private String resource1 = "";
    private String resource2 = "";
    private String resource3 = "";

}
