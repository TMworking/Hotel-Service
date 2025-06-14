package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.web.dto.facility.request.FacilityFilterRequest;
import org.example.web.dto.facility.response.FacilityPageResponse;
import org.example.mappers.FacilityMapper;
import org.example.web.dto.facility.request.FacilityRequest;
import org.example.web.dto.facility.response.FacilityResponse;
import org.example.exception.CsvExportException;
import org.example.model.domain.Facility;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebFacilityServiceImpl implements WebFacilityService {

    private final FacilityService facilityService;
    private final ExportService exportService;
    private final FacilityMapper facilityMapper;

    @Value("${facilitiesTomcatExportPath}")
    private String facilitiesExportPath;

    @Override
    public void exportFacilities() {
        try {
            List<Facility> facilityList = facilityService.getFacilities();
            exportService.exportToCsv(facilityList, facilitiesExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export facilities: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> saveFacility(FacilityRequest request) {
        facilityService.saveFacility(request.getCost(), request.getFacilityType());
        return ResponseEntity.ok("Facility successfully saved");
    }

    @Override
    public FacilityPageResponse getAllFacilities(FacilityFilterRequest request) {
        List<Facility> facilities = facilityService.getFacilities(request.getPageNumber(), request.getPageSize());
        long totalRecords = facilityService.countAll();
        return facilityMapper.toPageResponse(facilities, request.getPageNumber(), request.getPageSize(), totalRecords);
    }

    @Override
    public FacilityResponse getFacilityById(Long id) {
        Facility facility = facilityService.getById(id);
        return facilityMapper.toResponse(facility);
    }
}
