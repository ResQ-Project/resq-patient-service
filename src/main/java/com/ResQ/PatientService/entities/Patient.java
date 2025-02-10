package com.resq.PatientService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient {
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

    private String criticality;
    private boolean admission_status;
    private Integer assigned_doctor;

    // One Patient can have multiple allocated resources
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceAllocation> resources;
}
