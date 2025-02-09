package com.resq.PatientService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAllocationDto {
    private Integer resourceId;  // Resource ID
    private Integer allocatedUnits;  // Number of units assigned
}
