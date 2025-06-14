package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.FacilityDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Facility;
import org.example.model.enums.FacilityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityDao sqlFacilityDao;

    @Override
    public Facility getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find facility by id: {}", id);
        Facility facility = sqlFacilityDao.findById(id);
        if (facility == null) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Facility with {0} id not found", id));
        }
        log.debug("Facility found: {}", facility);
        return facility;
    }

    private Facility createFacility(BigDecimal cost, FacilityType type) {
        log.debug("Creating new facility: cost={}, type={}", cost, type);
        return new Facility(cost, type);
    }

    @Transactional
    @Override
    public Facility saveFacility(BigDecimal cost, FacilityType type) {
        log.debug("Saving new facility: cost={}, type={}", cost, type);
        Facility facility = sqlFacilityDao.save(createFacility(cost, type));
        log.debug("Facility saved successfully: {}", facility);
        return facility;
    }

    @Transactional
    @Override
    public void saveAllFacilities(List<Facility> facilities) {
        log.debug("Saving facilities list: {}", facilities);
        sqlFacilityDao.saveAll(facilities);
        log.debug("Facilities saved successfully: {}", facilities);
    }

    @Transactional
    @Override
    public void deleteAllFacilities() {
        log.debug("Deleting all facilities");
        sqlFacilityDao.deleteAll();
        log.debug("Facilities deleted successfully");
    }

    @Override
    public void printFacilitiesSortedByCost() {
        log.debug("Fetching facilities sorted by cost");
        List<Facility> facilitiesByCost = sqlFacilityDao.findFacilitiesSortedByCost();
        System.out.println(facilitiesByCost);
        log.debug("Facilities sorted by cost: {}", facilitiesByCost);
    }

    @Override
    public List<Facility> getFacilities() {
        log.debug("Fetching all facilities");
        List<Facility> facilities = sqlFacilityDao.findAll();
        log.debug("Fetched {} facilities", facilities.size());
        return facilities;
    }

    @Override
    public List<Facility> getFacilities(int pageNumber, int pageSize) {
        log.debug("Fetching all facilities with filtering");
        List<Facility> facilities = sqlFacilityDao.findAll(pageNumber, pageSize);
        log.debug("Fetched {} facilities with filtering", facilities.size());
        return facilities;
    }

    @Override
    public Long countAll() {
        return sqlFacilityDao.countAll();
    }
}
