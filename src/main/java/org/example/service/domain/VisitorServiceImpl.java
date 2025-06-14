package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.VisitorDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Visitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorServiceImpl implements VisitorService {

    private final VisitorDao sqlVisitorDao;

    @Override
    public Visitor getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find visitor by id: {}", id);
        Visitor visitor = sqlVisitorDao.findById(id);
        if (visitor == null) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Visitor with {0} id not found", id));
        }
        log.debug("Visitor found: {}", visitor);
        return visitor;
    }

    private Visitor createVisitor(String name, String surname, String passport) {
        log.debug("Creating new visitor: name={}, surname={}, passport={}", name, surname, passport);
        return new Visitor(name, surname, passport);
    }

    @Transactional
    @Override
    public Visitor saveVisitor(String name, String surname, String passport) {
        log.debug("Saving new visitor: name={}, surname={}, passport={}", name, surname, passport);
        Visitor visitor = sqlVisitorDao.save(createVisitor(name, surname, passport));
        log.debug("Visitor saved successfully: {}", visitor);
        return visitor;
    }

    @Transactional
    @Override
    public void saveAllVisitors(List<Visitor> visitors) {
        log.debug("Saving visitors list: {}", visitors);
        sqlVisitorDao.saveAll(visitors);
        log.debug("Visitors saved successfully: {}", visitors);
    }

    @Transactional
    @Override
    public void deleteAllVisitors() {
        log.debug("Deleting all visitors");
        sqlVisitorDao.deleteAll();
        log.debug("Visitors deleted successfully");
    }

    @Override
    public List<Visitor> getVisitorsSortedByName() {
        log.debug("Fetching visitors sorted by name");
        List<Visitor> visitorsByFilter = sqlVisitorDao.findVisitorsSortedByName();
        log.debug("Visitors sorted by name: {}", visitorsByFilter);
        return visitorsByFilter;
    }

    @Override
    public List<Facility> getVisitorFacilities(Visitor visitor) {
        log.debug("Fetching facilities for visitor with id: {}", visitor);

        List<Facility> visitorFacilities = visitor.getFacilityOrderList().stream()
                .map(FacilityOrder::getFacility)
                .collect(Collectors.toList());

        log.debug("Facilities for visitor with id {}: {}", visitor, visitorFacilities);
        return visitorFacilities;
    }

    @Override
    public List<Booking> getVisitorsBookings(Visitor visitor) {
        log.debug("Fetching bookings for visitor with id: {}", visitor);
        List<Booking> bookings = sqlVisitorDao.findVisitorsBookings(visitor);
        log.debug("Bookings for visitor with id {}", visitor);
        return bookings;
    }

    @Override
    public List<Visitor> getVisitors(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        log.debug("Fetching all visitors with sorting");
        List<Visitor> visitors = sqlVisitorDao.findAll(pageNumber, pageSize, sortBy, sortDirection);
        log.debug("Fetched {} visitors with sorting", visitors.size());
        return visitors;
    }

    @Override
    public List<Visitor> getVisitors() {
        log.debug("Fetching all visitors");
        List<Visitor> visitors = sqlVisitorDao.findAll();
        log.debug("Fetched {} visitors", visitors.size());
        return visitors;
    }

    @Override
    public Long countAll() {
        return sqlVisitorDao.countAll();
    }
}
