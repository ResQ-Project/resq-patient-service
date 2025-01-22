package com.resq.PatientService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resq.PatientService.dtos.PatientDto;
import com.resq.PatientService.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/savePatient")
    public void savePatient(@RequestBody String rawJson) throws JsonProcessingException {
        // Manually deserialize the raw JSON into PatientDto for debugging
        ObjectMapper objectMapper = new ObjectMapper();
        PatientDto patientDto = objectMapper.readValue(rawJson, PatientDto.class);

        patientService.savePatient(patientDto);
    }
}
