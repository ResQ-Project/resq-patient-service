package com.resq.PatientService.services;

import com.resq.PatientService.dtos.PatientDto;
import com.resq.PatientService.entities.Patient;
import com.resq.PatientService.repo.PatientRepo;
import com.resq.PatientService.utils.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ModelMapper modelMapper;

    public void savePatient(PatientDto patientData) {

        if(patientRepo.existsById(patientData.getPatient_id())){
            System.out.println("Duplicate Exists and test!");
        }

        patientRepo.save(modelMapper.map(patientData, Patient.class));
        System.out.println("Patient Saved Successfully");
    }

}
