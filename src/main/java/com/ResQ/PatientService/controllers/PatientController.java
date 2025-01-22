package com.resq.PatientService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resq.PatientService.dtos.PatientDto;
import com.resq.PatientService.dtos.ResponseDto;
import com.resq.PatientService.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResponseDto responseDto;

    //getting a single patient
    @GetMapping("/getPatientById/{patientId}")
    public ResponseDto getPatientById(@PathVariable int patientId){
        PatientDto patientDto = patientService.getPatientById(patientId);

        if(patientDto != null){
            responseDto.setStatus_code("200");
            responseDto.setMessage("Patient found successfully");
            responseDto.setData(patientDto);
        }else{
            responseDto.setStatus_code("404");
            responseDto.setMessage("Patient not found");
            responseDto.setData(null);
        }
        return responseDto;
    }

    //get all the patients in a ward
    @GetMapping("/getAllPatientsOfSingleWard/{wardNumber}")
    public ResponseDto getAllPatientsOfSingleWard(@PathVariable int wardNumber){
        List<PatientDto> patientDtoList = patientService.getAllPatientsOfSingleWard(wardNumber);

        if(patientDtoList.size() > 0){
            responseDto.setStatus_code("200");
            responseDto.setMessage("Patients found successfully");
            responseDto.setData(patientDtoList);
        }else{
            responseDto.setStatus_code("404");
            responseDto.setMessage("No patients found");
            responseDto.setData(null);
        }
        return responseDto;
    }

    //save a single patient
    @PostMapping("/savePatient")
    public ResponseDto savePatient(@RequestBody PatientDto patientDto){

        String res = patientService.savePatient(patientDto);

        if(res.equals("00")){
            responseDto.setStatus_code("201");
            responseDto.setMessage("Patient saved successfully");
            responseDto.setData(patientDto);
        }else if(res.equals("01")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient Already Exists");
            responseDto.setData(patientDto);
        } else{
            responseDto.setStatus_code("400");
            responseDto.setMessage("Error");
            responseDto.setData(null);
        }

        return responseDto;
    }
}
