package org.example.dao;

import org.example.model.domain.Facility;

import java.util.List;

public interface FacilityDao {
    Facility findById(Long id);

    List<Facility> findAll(int pageNumber, int pageSize);

    List<Facility> findAll();

    Long countAll();

    Facility save(Facility entity);

    void saveAll(List<Facility> facilities);

    void update(Facility entity);

    void delete(Facility facility);

    void deleteAll();

    List<Facility> findFacilitiesSortedByCost();
}
