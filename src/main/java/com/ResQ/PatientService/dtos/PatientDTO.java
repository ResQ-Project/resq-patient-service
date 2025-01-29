package com.resq.PatientService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDto {
    //primary properties
    private Integer national_id;
    private String first_name;
    private String last_name;
    private Integer age;
    private String gender;
    private String address;
    private String contact_number;
    private Integer ward_number;
    private boolean isDeleted;
    //other properties
    private String criticality;
    private boolean admission_status;
    private String assigned_doctor;
    private String resource1;
    private String resource2;
    private String resource3;
}
