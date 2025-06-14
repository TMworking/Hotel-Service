package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.facilityorder.request.FacilityOrderFilterRequest;
import org.example.web.dto.facilityorder.request.FacilityOrderRequest;
import org.example.web.dto.facilityorder.response.FacilityOrderPageResponse;
import org.example.web.dto.facilityorder.response.FacilityOrderResponse;
import org.example.service.web.WebFacilityOrderService;
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
@RequestMapping("/api/v1/facility-orders")
@RequiredArgsConstructor
public class FacilityOrderController {

    private final WebFacilityOrderService webFacilityOrderService;

    @GetMapping("/export")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> exportFacilityOrders() {
        webFacilityOrderService.exportFacilityOrders();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/all")
    public ResponseEntity<FacilityOrderPageResponse> getAllFacilityOrders(@Valid @RequestBody FacilityOrderFilterRequest request) {
        FacilityOrderPageResponse facilityOrderResponses = webFacilityOrderService.getAllFacilityOrders(request);
        return ResponseEntity.ok(facilityOrderResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityOrderResponse> getFacilityOrderById(@PathVariable("id") Long id) {
        FacilityOrderResponse response = webFacilityOrderService.getFacilityOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createFacilityOrder(@Valid @RequestBody FacilityOrderRequest request) {
        return webFacilityOrderService.saveFacilityOrder(request);
    }
}
