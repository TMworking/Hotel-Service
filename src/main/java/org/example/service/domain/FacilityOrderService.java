package org.example.service.domain;

import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Visitor;

import java.time.LocalDateTime;
import java.util.List;

public interface FacilityOrderService {
    FacilityOrder getById(Long id) throws EntityNotFoundException;

    FacilityOrder saveFacilityOrder(Facility facility, Visitor visitor, LocalDateTime date) throws EntityNotFoundException;

    void saveAllFacilityOrders(List<FacilityOrder> facilityOrders);

    void deleteAllFacilityOrders();

    List<FacilityOrder> getFacilityOrdersSortedByDate();

    List<FacilityOrder> getFacilityOrders(int pageNumber, int pageSize, String sortBy, String sortDirection);

    List<FacilityOrder> getFacilityOrders();

    Long countAll();
}
