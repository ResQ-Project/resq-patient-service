package com.ResQ.PatientService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
