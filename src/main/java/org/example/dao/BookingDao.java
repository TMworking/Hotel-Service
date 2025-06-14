package org.example.dao;

import org.example.model.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingDao {
    Optional<Booking> findById(Long id);

    List<Booking> findAll();

    List<Booking> findAll(int pageNumber, int pageSize);

    Long countAll();

    Booking save(Booking entity);

    void saveAll(List<Booking> bookings);

    void update(Booking entity);

    void delete(Booking booking);

    void deleteAll();
}
