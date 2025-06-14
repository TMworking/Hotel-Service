package org.example.dao;

import org.example.model.domain.FacilityOrder;

import java.util.List;
import java.util.Optional;

public interface FacilityOrderDao {
    Optional<FacilityOrder> findById(Long id);

    List<FacilityOrder> findAll(int pageNumber, int pageSize, String sortBy, String sortDirection);

    List<FacilityOrder> findAll();

    Long countAll();

    FacilityOrder save(FacilityOrder entity);

    void saveAll(List<FacilityOrder> facilityOrders);

    void update(FacilityOrder entity);

    void delete(FacilityOrder facilityOrder);

    void deleteAll();

    List<FacilityOrder> findFacilityOrdersSortedByDate();
}
