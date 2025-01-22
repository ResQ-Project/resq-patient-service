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
    public PatientDto getPatientById(int patientId){
        if(patientRepo.existsById(patientId)){
            Patient patient = patientRepo.findById(patientId).get();
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
            if(patientRepo.existsById(patientData.getPatient_id())){
                return VarList.RSP_DUPLICATE;
            }else{
                patientRepo.save(modelMapper.map(patientData, Patient.class));
                return VarList.RSP_SUCCESS;
            }
        }catch (Exception e){
            return VarList.RSP_ERROR;
        }
    }

}
