package org.example.service.domain;

import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.Visitor;

import java.util.List;

public interface VisitorService {

    Visitor getById(Long id) throws EntityNotFoundException;

    Visitor saveVisitor(String name, String surname, String passport);

    void saveAllVisitors(List<Visitor> visitors);

    void deleteAllVisitors();

    List<Visitor> getVisitorsSortedByName();

    List<Facility> getVisitorFacilities(Visitor visitor);

    List<Booking> getVisitorsBookings(Visitor visitor);

    List<Visitor> getVisitors(int pageNumber, int pageSize, String sortBy, String sortDirection);

    List<Visitor> getVisitors();

    Long countAll();
}
