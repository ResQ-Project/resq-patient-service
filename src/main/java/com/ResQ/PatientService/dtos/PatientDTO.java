package com.resq.PatientService.dtos;

import com.resq.PatientService.dtos.ResourceAllocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Integer national_id;
    private String first_name;
    private String last_name;
    private Integer age;
    private String gender;
    private String address;
    private String contact_number;
    private Integer ward_number;
    private String criticality;
    private boolean admission_status;
    private String assigned_doctor;

    private List<ResourceAllocationDto> resources; // Now holds resource objects
}
