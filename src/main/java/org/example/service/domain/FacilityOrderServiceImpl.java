package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.FacilityOrderDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Visitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityOrderServiceImpl implements FacilityOrderService {

    private final FacilityOrderDao sqlFacilityOrderDao;

    @Override
    public FacilityOrder getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find facility order by id: {}", id);
        FacilityOrder facilityOrder = sqlFacilityOrderDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Facility Order with {0} id not exist", id))
                );
        log.debug("Facility order found: {}", facilityOrder);
        return facilityOrder;
    }

    private FacilityOrder createFacilityOrder(Facility facility, Visitor visitor, LocalDateTime date) {
        log.debug("Creating new facility order: facilityId={}, visitorId={}, date={}", facility, visitor, date);

        FacilityOrder order = new FacilityOrder();
        order.setDate(date);

        order.setVisitor(visitor);
        order.setFacility(facility);

        log.debug("Facility order created: {}", order);
        return order;
    }

    @Transactional
    @Override
    public FacilityOrder saveFacilityOrder(Facility facility, Visitor visitor, LocalDateTime date) {
        log.debug("Saving new facility order: facilityId={}, visitorId={}, date={}", facility, visitor, date);
        FacilityOrder facilityOrder = sqlFacilityOrderDao.save(createFacilityOrder(facility, visitor, date));
        log.debug("Facility order saved successfully: {}", facilityOrder);
        return facilityOrder;
    }

    @Transactional
    @Override
    public void saveAllFacilityOrders(List<FacilityOrder> facilityOrders) {
        log.debug("Saving facility orders list: {}", facilityOrders);
        sqlFacilityOrderDao.saveAll(facilityOrders);
        log.debug("Facility orders saved successfully: {}", facilityOrders);
    }

    @Transactional
    @Override
    public void deleteAllFacilityOrders() {
        log.debug("Deleting all facility orders");
        sqlFacilityOrderDao.deleteAll();
        log.debug("Facility orders deleted successfully");
    }

    @Override
    public List<FacilityOrder> getFacilityOrdersSortedByDate() {
        log.debug("Fetching facility orders sorted by date");
        List<FacilityOrder> facilityOrders = sqlFacilityOrderDao.findFacilityOrdersSortedByDate();
        log.debug("Facility orders sorted by date: {}", facilityOrders);
        return facilityOrders;
    }

    @Override
    public List<FacilityOrder> getFacilityOrders() {
        log.debug("Fetching all facility orders");
        List<FacilityOrder> facilityOrders = sqlFacilityOrderDao.findAll();
        log.debug("Fetched {} facility orders", facilityOrders.size());
        return facilityOrders;
    }

    @Override
    public List<FacilityOrder> getFacilityOrders(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        log.debug("Fetching all facility orders with filtering");
        List<FacilityOrder> facilityOrders = sqlFacilityOrderDao.findAll(pageNumber, pageSize, sortBy, sortDirection);
        log.debug("Fetched {} facility orders with filtering", facilityOrders.size());
        return facilityOrders;
    }

    @Override
    public Long countAll() {
        return sqlFacilityOrderDao.countAll();
    }
}
