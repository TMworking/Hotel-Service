package org.example.service.web;

import org.example.web.dto.facilityorder.request.FacilityOrderFilterRequest;
import org.example.web.dto.facilityorder.request.FacilityOrderRequest;
import org.example.web.dto.facilityorder.response.FacilityOrderPageResponse;
import org.example.web.dto.facilityorder.response.FacilityOrderResponse;
import org.springframework.http.ResponseEntity;

public interface WebFacilityOrderService {

    void exportFacilityOrders();

    FacilityOrderPageResponse getAllFacilityOrders(FacilityOrderFilterRequest request);

    FacilityOrderResponse getFacilityOrderById(Long id);

    ResponseEntity<String> saveFacilityOrder(FacilityOrderRequest request);
}
