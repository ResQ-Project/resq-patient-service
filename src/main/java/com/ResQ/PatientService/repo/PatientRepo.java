package com.resq.PatientService.repo;

import com.resq.PatientService.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepo extends JpaRepository<Patient, Integer> {

    //find all the patients related to the ward
    @Query("SELECT p FROM Patient p WHERE p.ward_number = :wardNumber")
    List<Patient> findAllByWardNumber(@Param("wardNumber") Integer wardNumber);


}
