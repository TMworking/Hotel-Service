package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.BookingDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.model.enums.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final RoomService roomService;
    private final BookingDao sqlBookingDao;

    @Override
    public Booking getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find booking by id: {}", id);
        Booking booking = sqlBookingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Booking with {0} id not exist", id))
                );
        log.debug("Booking found: {}", booking);
        return booking;
    }

    private Booking createBooking(Room room, Visitor visitor, LocalDateTime settleDate, Integer duration) {
        log.debug("Creating new booking: roomId={}, visitorId={}, settleDate={}, duration={}", room, visitor, settleDate, duration);

        Booking booking = new Booking();
        booking.setSettleDate(settleDate);
        booking.setDuration(duration);

        booking.setRoom(room);
        booking.setVisitor(visitor);

        log.debug("Booking created: {}", booking);
        return booking;
    }

    @Transactional
    @Override
    public Booking saveBooking(Room room, Visitor visitor, LocalDateTime settleDate, Integer duration) {
        log.debug("Saving new booking: roomId={}, visitorId={}, settleDate={}, duration={}", room, visitor, settleDate, duration);
        Booking booking = sqlBookingDao.save(createBooking(room, visitor, settleDate, duration));
        log.debug("Booking saved successfully: {}", booking);
        return booking;
    }

    @Transactional
    @Override
    public void saveAllBookings(List<Booking> bookings) {
        log.debug("Saving booking list: {}", bookings);
        sqlBookingDao.saveAll(bookings);
        log.debug("Bookings saved successfully: {}", bookings);
    }

    @Transactional
    @Override
    public void deleteAllBookings() {
        log.debug("Deleting all bookings");
        sqlBookingDao.deleteAll();
        log.debug("Bookings deleted successfully");
    }

    @Transactional
    @Override
    public void settleVisitorInRoom(Room room, Visitor visitor, Integer duration, LocalDateTime settleDate) {
        log.debug("Settling visitor in room:, visitorId={}, duration={}, settleDate={}", visitor, duration, settleDate);
        if (room.getStatus().equals(RoomStatus.OCCUPIED)) {
            throw new IllegalStateException("Room already occupied");
        }
        room.setStatus(RoomStatus.OCCUPIED);
        roomService.updateRoom(room);
        saveBooking(room, visitor, settleDate, duration);
        log.debug("Visitor settled in room successfully: room={}, visitorId={}", room, visitor);
    }

    @Transactional
    @Override
    public void evictVisitorFromRoom(Booking booking) {
        log.debug("Evicting visitor from booking with id={}", booking);
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.FREE);
        roomService.updateRoom(room);
        log.debug("Visitor evicted from booking with id={}", booking);
    }

    @Override
    public List<Booking> getBookings() {
        log.debug("Fetching all bookings");
        List<Booking> bookings = sqlBookingDao.findAll();
        log.debug("Fetched {} bookings", bookings.size());
        return bookings;
    }

    @Override
    public List<Booking> getBookings(int pageNumber, int pageSize) {
        log.debug("Fetching all bookings with pagination");
        List<Booking> bookings = sqlBookingDao.findAll(pageNumber, pageSize);
        log.debug("Fetched {} bookings with pagination", bookings.size());
        return bookings;
    }

    @Override
    public Long countAll() {
        return sqlBookingDao.countAll();
    }
}
