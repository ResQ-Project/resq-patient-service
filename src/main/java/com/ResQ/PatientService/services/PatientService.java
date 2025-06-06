package com.resq.PatientService.services;

import com.resq.PatientService.OutsourcedServices.ResourceService.ResourceInterface;
import com.resq.PatientService.OutsourcedServices.StaffService.StaffInterface;
import com.resq.PatientService.dtos.PatientDto;
import com.resq.PatientService.dtos.ResourceAllocationDto;
import com.resq.PatientService.entities.Patient;
import com.resq.PatientService.entities.ResourceAllocation;
import com.resq.PatientService.repo.PatientRepo;
import com.resq.PatientService.utils.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ModelMapper modelMapper;

    //interfaces
    @Autowired
    ResourceInterface resourceInterface;

    @Autowired
    StaffInterface staffInterface;

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
        String returnStatement = "";
        try {
            // Check if patient already exists
            if (patientRepo.existsById(patientData.getNational_id())) {
                returnStatement = "Patient_With_Same_ID_Exist";
                return  returnStatement;
            }

            // Convert DTO to Patient entity
            Patient patient = modelMapper.map(patientData, Patient.class);


            //**********  Resource Allocation for newly added patient**********
            //define a new list for allocated resources (as there are more than one resource)
            List<ResourceAllocation> resourceAllocations = new ArrayList<>();

            // Iterate through resource IDs from request
            for (ResourceAllocationDto resourceAllocationDto : patientData.getResources()) {
                Object data = resourceInterface.getSingleResourceById(resourceAllocationDto.getResourceId()).getData();
                if (data == null) {
                    returnStatement = "THERE_IS_NO_RESOURCE_WITH_THAT_ID" + resourceAllocationDto.getResourceId();
                    return returnStatement;
                }

                //iterate through the resource data fetched
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data;

                if (map.containsKey("availableUnits")) {  // Correct key based on DB
                    int availableCount = (Integer) map.get("availableUnits");
                    System.out.println(availableCount);

                    // Check if enough resources are available
                    if (availableCount >= resourceAllocationDto.getAllocatedUnits()) {
                        // Update available units count
                        map.put("availableUnits", availableCount - resourceAllocationDto.getAllocatedUnits());
                        resourceInterface.updateResource(resourceAllocationDto.getResourceId(), map);

                        // Create ResourceAllocation entity
                        ResourceAllocation resourceAllocation = new ResourceAllocation();
                        resourceAllocation.setResourceId(resourceAllocationDto.getResourceId());
                        resourceAllocation.setAllocatedUnits(resourceAllocationDto.getAllocatedUnits());
                        resourceAllocation.setPatient(patient); // Link to Patient

                        // Add to list
                        resourceAllocations.add(resourceAllocation);
                    } else {
                        returnStatement = "INSUFFICIENT_RESOURCES" + resourceAllocationDto.getResourceId();
                        return returnStatement;
                    }
                } else {
                    returnStatement =  "Key 'available_units' is missing in the data for resource ID: " + resourceAllocationDto.getResourceId();
                    return returnStatement;
                }
            }
            System.out.println(resourceAllocations);
            // Set resource allocations to the patient
            patient.setResources(resourceAllocations);


            //**********update staff service as well with the new patientDetails*********
            Map<String, Object> associatePatientData = new HashMap<>();    // Create a map for patient data update
            associatePatientData.put("patientIds", Collections.singletonList(patientData.getNational_id()));

            // Call the Staff Service using Feign Client
            staffInterface.updateStaffMember(patientData.getAssigned_doctor(), associatePatientData);

            //**********Save the full patient details*********
            // Save patient along with resources
            patientRepo.save(patient);

            returnStatement = "SUCCESS";
            return returnStatement;
        } catch (Exception e) {
            e.printStackTrace(); // Debugging
            returnStatement = "Error Saving Patient";
        }
        return returnStatement;
    }


    //update a single patient
    public String updatePatient(int nationalId, PatientDto patientDto) {
        if (!patientRepo.existsById(nationalId)) {
            return "Patient_Not_Found";
        }

        // Get the existing patient
        Patient existingPatient = patientRepo.findById(nationalId).get();

        // Update basic patient details (excluding resources)
        existingPatient.setFirst_name(patientDto.getFirst_name());
        existingPatient.setLast_name(patientDto.getLast_name());
        existingPatient.setAge(patientDto.getAge());
        existingPatient.setGender(patientDto.getGender());
        existingPatient.setAddress(patientDto.getAddress());
        existingPatient.setContact_number(patientDto.getContact_number());
        existingPatient.setWard_number(patientDto.getWard_number());
        existingPatient.setCriticality(patientDto.getCriticality());
        existingPatient.setAdmission_status(patientDto.isAdmission_status());
        existingPatient.setAssigned_doctor(patientDto.getAssigned_doctor());

        // Track resources to remove
        List<Integer> updatedResourceIds = patientDto.getResources().stream()
                .map(ResourceAllocationDto::getResourceId)
                .toList();

        // Find resources to remove (not in the updated list)
        List<ResourceAllocation> resourcesToRemove = existingPatient.getResources().stream()
                .filter(r -> !updatedResourceIds.contains(r.getResourceId()))
                .toList();

        // Remove orphaned resource allocations (detach first)
        for (ResourceAllocation removedResource : resourcesToRemove) {
            Object existingResource = resourceInterface.getSingleResourceById(removedResource.getResourceId()).getData();
            LinkedHashMap<String, Object> mapExistingResource = (LinkedHashMap<String, Object>) existingResource;

            if (mapExistingResource.containsKey("availableUnits")) {
                int availableCount = (Integer) mapExistingResource.get("availableUnits");
                mapExistingResource.put("availableUnits", availableCount + removedResource.getAllocatedUnits());
                resourceInterface.updateResource(removedResource.getResourceId(), mapExistingResource);
            }

            removedResource.setPatient(null); // Properly detach orphan resource
        }

        // Remove orphaned resources BEFORE updating the list
        existingPatient.getResources().removeAll(resourcesToRemove);

        // Process new or updated resources
        for (ResourceAllocationDto resourceDto : patientDto.getResources()) {
            Object data = resourceInterface.getSingleResourceById(resourceDto.getResourceId()).getData();

            if (data == null) {
                return "Resource_Not_Found" + resourceDto.getResourceId() + " not found";
            }

            LinkedHashMap<String, Object> mapExistingResource = (LinkedHashMap<String, Object>) data;

            if (mapExistingResource.containsKey("availableUnits")) {
                int availableCount = (Integer) mapExistingResource.get("availableUnits");

                // Find existing allocation for the same resource
                ResourceAllocation existingAllocation = existingPatient.getResources()
                        .stream()
                        .filter(r -> r.getResourceId().equals(resourceDto.getResourceId()))
                        .findFirst()
                        .orElse(null);

                if (existingAllocation != null) {
                    // Update existing allocation count
                    int oldAllocatedUnits = existingAllocation.getAllocatedUnits();
                    int newAllocatedUnits = resourceDto.getAllocatedUnits();

                    if (newAllocatedUnits > oldAllocatedUnits) {
                        int additionalUnits = newAllocatedUnits - oldAllocatedUnits;
                        if (availableCount >= additionalUnits) {
                            mapExistingResource.put("availableUnits", availableCount - additionalUnits);
                            resourceInterface.updateResource(resourceDto.getResourceId(), mapExistingResource);
                        } else {
                            return "Insufficient_Resources:" + resourceDto.getResourceId();
                        }
                    } else if (newAllocatedUnits < oldAllocatedUnits) {
                        int freedUnits = oldAllocatedUnits - newAllocatedUnits;
                        mapExistingResource.put("availableUnits", availableCount + freedUnits);
                        resourceInterface.updateResource(resourceDto.getResourceId(), mapExistingResource);
                    }

                    existingAllocation.setAllocatedUnits(newAllocatedUnits);
                } else {
                    // If the resource is new, allocate it
                    if (availableCount >= resourceDto.getAllocatedUnits()) {
                        mapExistingResource.put("availableUnits", availableCount - resourceDto.getAllocatedUnits());
                        resourceInterface.updateResource(resourceDto.getResourceId(), mapExistingResource);

                        ResourceAllocation newResource = new ResourceAllocation();
                        newResource.setResourceId(resourceDto.getResourceId());
                        newResource.setAllocatedUnits(resourceDto.getAllocatedUnits());
                        newResource.setPatient(existingPatient);

                        existingPatient.getResources().add(newResource);
                    } else {
                        return "Insufficient_Resources:" + resourceDto.getResourceId();
                    }
                }
            } else {
                return "Not_Sufficient_Available_Units" + resourceDto.getResourceId();
            }
        }

        // Save updated patient and resources
        patientRepo.save(existingPatient);
        return "SUCCESS";
    }



    //delete a single patient
    public String deletePatient(int nationalId){
        String returnStatement = "";
        try {
            if (patientRepo.existsById(nationalId)) {
                // Retrieve the patient
                Patient existingPatient = patientRepo.findById(nationalId).get();

                // Loop through allocated resources
                for (ResourceAllocation resourceAllocation : existingPatient.getResources()) {
                    // Fetch the actual resource details
                    Object existingResource = resourceInterface.getSingleResourceById(resourceAllocation.getResourceId()).getData();

                    if (existingResource != null) {
                        LinkedHashMap<String, Object> mapExistingResource = (LinkedHashMap<String, Object>) existingResource;

                        // Update available units count
                        if (mapExistingResource.containsKey("availableUnits")) {
                            int availableCount = (Integer) mapExistingResource.get("availableUnits");
                            mapExistingResource.put("availableUnits", availableCount + resourceAllocation.getAllocatedUnits());

                            // Update resource table
                            resourceInterface.updateResource(resourceAllocation.getResourceId(), mapExistingResource);
                        }
                    }
                }

                // Remove allocated resources (this will delete records from `resource_allocations` table)
                existingPatient.getResources().clear();
                existingPatient.setDeleted(true);
                patientRepo.save(existingPatient); // Save to apply cascade deletion

                returnStatement = "SUCCESS";
                return returnStatement;
            } else {
                returnStatement = "Patient_Not_Found";
                return returnStatement;
            }
        } catch (Exception e) {
            returnStatement = "ERROR";
            return returnStatement;
        }
    }

}
