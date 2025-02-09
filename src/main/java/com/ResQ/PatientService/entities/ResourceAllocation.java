package com.resq.PatientService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resource_allocations")
public class ResourceAllocation {
    @Id
    private Integer resourceId;  // The actual resource ID
    private Integer allocatedUnits;  // Number of units assigned

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;  // Linking back to the patient
}
