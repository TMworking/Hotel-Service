package org.example.service.web;

import org.example.web.dto.facility.request.FacilityFilterRequest;
import org.example.web.dto.facility.request.FacilityRequest;
import org.example.web.dto.facility.response.FacilityPageResponse;
import org.example.web.dto.facility.response.FacilityResponse;
import org.springframework.http.ResponseEntity;


public interface WebFacilityService {

    void exportFacilities();

    ResponseEntity<String> saveFacility(FacilityRequest request);

    FacilityPageResponse getAllFacilities(FacilityFilterRequest request);

    FacilityResponse getFacilityById(Long id);
}
