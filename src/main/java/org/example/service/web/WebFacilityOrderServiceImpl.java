package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.web.dto.facilityorder.request.FacilityOrderFilterRequest;
import org.example.web.dto.facilityorder.response.FacilityOrderPageResponse;
import org.example.mappers.FacilityOrderMapper;
import org.example.web.dto.facilityorder.request.FacilityOrderRequest;
import org.example.web.dto.facilityorder.response.FacilityOrderResponse;
import org.example.exception.CsvExportException;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Visitor;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityOrderService;
import org.example.service.domain.FacilityService;
import org.example.service.domain.VisitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebFacilityOrderServiceImpl implements WebFacilityOrderService {

    private final FacilityOrderService facilityOrderService;
    private final VisitorService visitorService;
    private final FacilityService facilityService;
    private final ExportService exportService;
    private final FacilityOrderMapper facilityOrderMapper;

    @Value("${facilityOrdersTomcatExportPath}")
    private String facilityOrdersExportPath;

    @Override
    public void exportFacilityOrders() {
        try {
            List<FacilityOrder> facilityOrders = facilityOrderService.getFacilityOrders();
            exportService.exportToCsv(facilityOrders, facilityOrdersExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export Facility orders: " + e.getMessage());
        }
    }

    @Override
    public FacilityOrderPageResponse getAllFacilityOrders(FacilityOrderFilterRequest request) {
        List<FacilityOrder> facilityOrders = facilityOrderService.getFacilityOrders(
                request.getPageNumber(),
                request.getPageSize(),
                request.getSortBy(),
                request.getSortDirection());
        long totalRecords = facilityService.countAll();
        return facilityOrderMapper.toPageResponse(facilityOrders, request.getPageNumber(), request.getPageSize(), totalRecords);
    }

    @Override
    public FacilityOrderResponse getFacilityOrderById(Long id) {
        FacilityOrder facilityOrder = facilityOrderService.getById(id);
        return facilityOrderMapper.toResponse(facilityOrder);
    }

    @Override
    public ResponseEntity<String> saveFacilityOrder(FacilityOrderRequest request) {
        Visitor visitor = visitorService.getById(request.getVisitorId());
        Facility facility = facilityService.getById(request.getFacilityId());
        facilityOrderService.saveFacilityOrder(facility, visitor, request.getOrderDate());
        return ResponseEntity.ok("Facility order successfully saved");
    }
}
