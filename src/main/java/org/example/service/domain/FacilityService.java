package org.example.service.domain;

import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Facility;
import org.example.model.enums.FacilityType;

import java.math.BigDecimal;
import java.util.List;

public interface FacilityService {

    Facility getById(Long id) throws EntityNotFoundException;

    Facility saveFacility(BigDecimal cost, FacilityType type);

    void saveAllFacilities(List<Facility> facilities);

    void deleteAllFacilities();

    void printFacilitiesSortedByCost();

    List<Facility> getFacilities(int pageNumber, int pageSize);

    List<Facility> getFacilities();

    Long countAll();
}
