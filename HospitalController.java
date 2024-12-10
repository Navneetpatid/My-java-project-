package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.responsepayload.GetAllHospital;
import com.janaushadhi.adminservice.serviceimpl.HospitalServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/hospital")
public class HospitalController {

    @Autowired
    private HospitalServiceImpl hospitalService;

    @PostMapping( value = "/getAllHospital")
    public Map<String,Object> getAllHospital(@RequestBody GetAllHospital getAllHospital){
        return hospitalService.getAllHospital(getAllHospital);
    }
}
