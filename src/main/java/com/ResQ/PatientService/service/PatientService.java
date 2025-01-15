package com.ResQ.PatientService.service;

import com.ResQ.PatientService.dto.PatientDTO;
import com.ResQ.PatientService.entities.Patient;
import com.ResQ.PatientService.repo.PatientRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService {
    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ModelMapper modelMapper;

    public PatientDTO addPatient(PatientDTO patientDTO) {
        patientRepo.save(modelMapper.map(patientDTO, Patient.class));
        return patientDTO;
    }
}
