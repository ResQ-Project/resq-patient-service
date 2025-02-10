package com.resq.PatientService.OutsourcedServices.ResourceService;

import com.resq.PatientService.dtos.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;

@FeignClient("RESOURCESERVICE")
public interface ResourceInterface {
    //get details about existing resources
    @GetMapping("api/v1/resource/getSingleResource/{resource_id}")
    public ResponseDto getSingleResourceById(@PathVariable Integer resource_id);

    //update the resources
    @PutMapping("api/v1/resource/updateResource/{resource_id}")
    public ResponseDto updateResource(@PathVariable Integer resource_id, @RequestBody LinkedHashMap<String, Object> resourceDto);
}

