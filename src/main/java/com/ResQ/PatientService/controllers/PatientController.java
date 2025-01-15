package com.ResQ.PatientService.controllers;

import com.ResQ.PatientService.dto.PatientDTO;
import com.ResQ.PatientService.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/savePatient")
    public PatientDTO savePatient(@RequestBody PatientDTO patientDTO) {
        System.out.println(patientDTO);
        return patientService.addPatient(patientDTO);
    }
}
