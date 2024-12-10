package com.janaushadhi.adminservice.controller;
import com.janaushadhi.adminservice.requestpayload.ExecutiveCouncilRequest;
import com.janaushadhi.adminservice.requestpayload.GoverningCouncilRequest;
import com.janaushadhi.adminservice.requestpayload.ManagementRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllExcecutive;
import com.janaushadhi.adminservice.responsepayload.GetAllGoverning;
import com.janaushadhi.adminservice.responsepayload.GetAllManagement;
import com.janaushadhi.adminservice.serviceimpl.GovernanceServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/Governance")
public class GovernanceController {


    @Autowired
    private GovernanceServiceImpl governanceService;
    @PostMapping(value = "/addGoverningCouncil")
    public ResponseEntity< Object> addGoverningCouncil(@RequestBody GoverningCouncilRequest governingCouncilRequest) {
        return  ResponseEntity.ok(governanceService.addGoverningCouncil(governingCouncilRequest)) ;
    }

    @PostMapping( value = "/getAllGoverningCouncil")
    public Map<String,Object> getAllCouncil( @RequestBody GetAllGoverning getAllGoverning){
        return governanceService.getAllCouncil(getAllGoverning);
    }


    @PostMapping(value = "/addExecutiveCouncil")
    public ResponseEntity< Object> addExecutiveCouncil(@RequestBody ExecutiveCouncilRequest executiveCouncilRequest) {
        return  ResponseEntity.ok(governanceService.addExecutiveCouncil(executiveCouncilRequest)) ;
    }

    @PostMapping( value = "/getAllExecutiveCouncil")
    public Map<String,Object> getAllExecutiveCouncil(@RequestBody GetAllExcecutive getAllExcecutive){
        return governanceService.getAllExecutiveCouncil(getAllExcecutive);
    }


    @PostMapping(value = "/addManagementTeam")
    public ResponseEntity< Object> addManagementTeam(@RequestBody ManagementRequest managementRequest) throws IOException {
        return  ResponseEntity.ok(governanceService.addManagementTeam(managementRequest)) ;
    }

    @GetMapping(value = "/getByManagementId")
    public Map<String, Object> getByManagementId(Long id) {
        return governanceService.getByManagementId(id);
    }


    @PostMapping( value = "/getAllManagementTeam")
    public Map<String,Object> getAllManagement(@RequestBody GetAllManagement getAllManagement){
        return governanceService.getAllManagement(getAllManagement);
    }
    @DeleteMapping(value = "/deleteByManagementId")
    public Map<String, Object> deleteByManagementId(@RequestParam Long id) {
        return governanceService.deleteByManagementId(id);
    }
    @PostMapping(value = "/managementTeamImageActiveDeactiveById")
    public Map<String, Object> managementTeamImageActiveDeactiveById(Long id,boolean imageIsActive) {
        return governanceService.managementTeamImageActiveDeactiveById(id,imageIsActive);
    }
}
