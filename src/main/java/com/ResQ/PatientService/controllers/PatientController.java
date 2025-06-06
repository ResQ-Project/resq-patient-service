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
    @GetMapping("/getPatientById/{nationalId}")
    public ResponseDto getPatientById(@PathVariable int nationalId){
        PatientDto patientDto = patientService.getPatientById(nationalId);

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

        if(res.equals("SUCCESS")){
            responseDto.setStatus_code("201");
            responseDto.setMessage("Patient saved successfully");
            responseDto.setData(patientDto);
        }else if(res.equals("Patient_With_Same_ID_Exist")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient Already Exists with that National ID");
            responseDto.setData(null);
        }else if(res.contains("THERE_IS_NO_RESOURCE_WITH_THAT_ID")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient cannot be saved as there is no resource exist with that resource ID");
            responseDto.setData(null);
        }else if(res.contains("INSUFFICIENT_RESOURCES")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient cannot be saved because of insufficient resources");
            responseDto.setData(null);
        }else{
            responseDto.setStatus_code("400");
            responseDto.setMessage("Error");
            responseDto.setData(null);
        }

        return responseDto;
    }

    //update a single patient
    @PutMapping("/updatePatient/{nationalId}")
    public ResponseDto updatePatient(@PathVariable int nationalId, @RequestBody PatientDto patientDto){
        String res = patientService.updatePatient(nationalId, patientDto);
        if(res.equals("SUCCESS")){
            responseDto.setStatus_code("201");
            responseDto.setMessage("Patient updated successfully");
            responseDto.setData(patientDto);
        } else if(res.contains("Patient_Not_Found")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient Not Found");
            responseDto.setData(null);
        }else if(res.contains("Resource_Not_Found")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Resource with that resource ID is not found");
            responseDto.setData(null);
        }else if(res.contains("Insufficient_Resources:")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Insufficient Resources available for update");
            responseDto.setData(null);
        } else if(res.contains("Not_Sufficient_Available_Units")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Insufficient Resources available for update");
            responseDto.setData(null);
        }else{
            responseDto.setStatus_code("400");
            responseDto.setMessage("Error updating Patient");
            responseDto.setData(null);
        }

        return responseDto;
    }


    //delete a single patient
    @PatchMapping("/deletePatient/{nationalId}")
    public ResponseDto deletePatient(@PathVariable int nationalId){
        String res = patientService.deletePatient(nationalId);
        if(res.equals("SUCCESS")){
            responseDto.setStatus_code("201");
            responseDto.setMessage("Patient Deleted successfully");
            responseDto.setData(null);
        } else if(res.contains("Patient_Not_Found")){
            responseDto.setStatus_code("400");
            responseDto.setMessage("Patient Not Found");
            responseDto.setData(null);
        }else{
            responseDto.setStatus_code("400");
            responseDto.setMessage("Error");
            responseDto.setData(null);
        }

        return responseDto;
    }
}
