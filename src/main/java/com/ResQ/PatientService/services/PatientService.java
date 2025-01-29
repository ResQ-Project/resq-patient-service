package com.resq.PatientService.services;

import com.resq.PatientService.dtos.PatientDto;
import com.resq.PatientService.entities.Patient;
import com.resq.PatientService.repo.PatientRepo;
import com.resq.PatientService.utils.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ModelMapper modelMapper;

    //return only a single patient
    public PatientDto getPatientById(int nationalId){
        if(patientRepo.existsById(nationalId)){
            Patient patient = patientRepo.findById(nationalId).get();
            return modelMapper.map(patient, PatientDto.class);
        }else{
            return null;
        }
    }

    //get all the patients related to a ward
    public List<PatientDto> getAllPatientsOfSingleWard(int wardNumber){
        //need to get all the patients in a ward
        List<Patient> patientsList = patientRepo.findAllByWardNumber(wardNumber);
        return modelMapper.map(patientsList, new TypeToken<ArrayList<PatientDto>>(){}.getType());
    }

    //save a single patient
    public String savePatient(PatientDto patientData) {
        try{
            if(patientRepo.existsById(patientData.getNational_id())){
                return VarList.RSP_DUPLICATE;
            }else{
                patientRepo.save(modelMapper.map(patientData, Patient.class));
                return VarList.RSP_SUCCESS;
            }
        }catch (Exception e){
            return VarList.RSP_ERROR;
        }
    }

    //update a single patient
    public String updatePatient(int nationalId, PatientDto patientDto){
       if(patientRepo.existsById(nationalId)){
           patientRepo.save(modelMapper.map(patientDto, Patient.class));
           return VarList.RSP_SUCCESS;
       }else{
           return VarList.RSP_ERROR;
       }
    }

    //delete a single patient
    public String deletePatient(int nationalId){
        try{
            if(patientRepo.existsById(nationalId)){
                Patient existingPatient = patientRepo.findById(nationalId).get();
                existingPatient.setDeleted(true);
                patientRepo.save(existingPatient);
                return VarList.RSP_SUCCESS;
            }else{
                return VarList.RSP_ERROR;
            }
        }catch (Exception e){
            return VarList.RSP_ERROR;
        }
    }

}
