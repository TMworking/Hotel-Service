package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.facility.request.FacilityFilterRequest;
import org.example.web.dto.facility.request.FacilityRequest;
import org.example.web.dto.facility.response.FacilityPageResponse;
import org.example.web.dto.facility.response.FacilityResponse;
import org.example.service.web.WebFacilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final WebFacilityService webFacilityService;

    @GetMapping("/export")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> exportVisitors() {
        webFacilityService.exportFacilities();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<String> createFacility(@Valid @RequestBody FacilityRequest request) {
        return webFacilityService.saveFacility(request);
    }

    @PostMapping("/all")
    public ResponseEntity<FacilityPageResponse> getAllFacilities(FacilityFilterRequest request) {
        FacilityPageResponse facilityResponses = webFacilityService.getAllFacilities(request);
        return ResponseEntity.ok(facilityResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityResponse> getFacilityById(@PathVariable("id") Long id) {
        FacilityResponse response = webFacilityService.getFacilityById(id);
        return ResponseEntity.ok(response);
    }
}
