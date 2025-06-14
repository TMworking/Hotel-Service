package org.example.service.domain;

import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking getById(Long id) throws EntityNotFoundException;

    Booking saveBooking(Room room, Visitor visitor, LocalDateTime settleDate, Integer duration);

    void saveAllBookings(List<Booking> bookings);

    void deleteAllBookings();

    void settleVisitorInRoom(Room room, Visitor visitor, Integer duration, LocalDateTime settleDate);

    void evictVisitorFromRoom(Booking booking);

    List<Booking> getBookings();

    List<Booking> getBookings(int pageNumber, int pageSize);

    Long countAll();
}
