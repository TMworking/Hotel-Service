package org.example.dao;

import org.example.model.domain.Booking;
import org.example.model.domain.Visitor;

import java.util.List;

public interface VisitorDao {
    Visitor findById(Long id);

    List<Visitor> findAll(int pageNumber, int pageSize, String sortBy, String sortDirection);

    List<Visitor> findAll();

    Long countAll();

    Visitor save(Visitor visitor);

    void saveAll(List<Visitor> visitors);

    void update(Visitor visitor);

    void delete(Visitor visitor);

    void deleteAll();

    List<Visitor> findVisitorsSortedByName();

    List<Visitor> findVisitorsSortedBySurname();

    List<Booking> findVisitorsBookings(Visitor visitor);
}
