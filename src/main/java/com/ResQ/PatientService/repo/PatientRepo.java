package com.ResQ.PatientService.repo;

import com.ResQ.PatientService.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patient, Integer> {
}
