package com.resq.PatientService.OutsourcedServices.StaffService;

import com.resq.PatientService.dtos.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("STAFFSERVICE")
public interface StaffInterface {
    @PutMapping("/api/v1/staff/updateStaffMember/{staffId}")
    public ResponseDto updateStaffMember(@PathVariable Integer staffId, @RequestBody Map<String, Object> updates);
}
