package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.RoomDao;
import org.example.service.RoomProperties;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomDao sqlRoomDao;
    private final RoomProperties roomProperties;

    @Override
    public RoomProperties getRoomProperties() {
        return roomProperties;
    }

    @Override
    @Transactional
    public void changeRoomStatus(Long id, RoomStatus status) {
        if (roomProperties.getStatusChangeEnabled()) {
            Room room = getById(id);
            room.setStatus(status);
            updateRoom(room);
        } else {
            throw new IllegalStateException("Changing room status is disabled");
        }
    }

    @Override
    public Room getById(Long id) throws EntityNotFoundException {
        log.debug("Attempting to find room by id: {}", id);
        Room room = sqlRoomDao.findById(id);
        if (room == null) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Room with {0} id not found", id));
        }
        log.debug("Room found by id: {}", room);
        return room;
    }

    @Override
    public Room getByNumber(String number) throws EntityNotFoundException {
        log.debug("Attempting to find room by number: {}", number);
        Room room = sqlRoomDao.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Room with {0} number not exist", number))
                );
        log.debug("Room found by number: {}", room);
        return room;
    }

    private Room createRoom(String roomNumber, BigDecimal price, RoomType type) {
        log.debug("Creating new room: roomNumber={}, price={}, type={}", roomNumber, price, type);
        return new Room(roomNumber, price, type);
    }

    @Transactional
    @Override
    public Room saveRoom(String roomNumber, BigDecimal price, RoomType type) {
        log.debug("Saving new room: roomNumber={}, price={}, type={}", roomNumber, price, type);
        Room room = sqlRoomDao.save(createRoom(roomNumber, price, type));
        log.debug("Room saved successfully: {}", room);
        return room;
    }

    @Transactional
    @Override
    public void saveAllRooms(List<Room> rooms) {
        log.debug("Saving rooms list: {}", rooms);
        sqlRoomDao.saveAll(rooms);
        log.debug("Rooms saved successfully: {}", rooms);
    }

    @Transactional
    @Override
    public void updateRoom(Room room) {
        log.debug("Updating room: {}", room);
        sqlRoomDao.update(room);
        log.debug("Room updated successfully: {}", room);
    }

    @Transactional
    @Override
    public void deleteAllRooms() {
        log.debug("Deleting all rooms");
        sqlRoomDao.deleteAll();
        log.debug("Rooms deleted successfully");
    }

    public List<Visitor> getLastRoomVisitors(Room room) {
        List<Booking> roomBookings = getRoomsBookings(room);

        roomBookings.sort(Comparator.comparing(
                this::getEvictDate,
                Comparator.reverseOrder()
        ));

        return roomBookings.stream()
                .limit(roomProperties.getHistorySize())
                .map(Booking::getVisitor)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getFreeRooms() {
        log.debug("Fetching free rooms");
        List<Room> freeRooms = sqlRoomDao.findFreeRooms();
        log.debug("Fetched {} free rooms", freeRooms.size());
        return freeRooms;
    }

    private LocalDateTime getEvictDate(Booking booking) {
        log.debug("Calculating evict date for booking: {}", booking);
        LocalDateTime evictDate = booking.getSettleDate().plusDays(booking.getDuration());
        log.debug("Evict date calculated: {}", evictDate);
        return evictDate;
    }

    @Override
    public LocalDateTime getRoomFreeDate(Room room) {
        log.debug("Calculating free date for room: {}", room);
        LocalDateTime maxDate = LocalDateTime.MIN;
        List<Booking> bookings = getRoomsBookings(room);
        for (Booking booking : bookings) {
            LocalDateTime evictDate = getEvictDate(booking);
            if (evictDate.isAfter(maxDate)) {
                maxDate = evictDate;
            }
        }
        maxDate = maxDate.equals(LocalDateTime.MIN) ? LocalDateTime.now().minusDays(1) : maxDate;
        log.debug("Room free date calculated: {}", maxDate);
        return maxDate;
    }

    @Override
    public List<Room> getFreeRoomsByDate(LocalDateTime date) {
        log.debug("Fetching free rooms by date: {}", date);
        List<Room> freeRooms = getRooms().stream()
                .filter(entry -> getRoomFreeDate(entry).isBefore(date))
                .collect(Collectors.toList());
        log.debug("Fetched {} free rooms by date {}", freeRooms.size(), date);
        return freeRooms;
    }

    @Override
    public List<Room> getFreeRoomsByType(RoomType roomType) {
        log.debug("Fetching free rooms by type: {}", roomType);
        List<Room> freeRoomsByType = sqlRoomDao.findFreeRoomsByType(roomType);
        log.debug("Fetched {} free rooms of type {}", freeRoomsByType.size(), roomType);
        return freeRoomsByType;
    }

    @Override
    public List<Room> getRoomsSortedByPrice() {
        log.debug("Fetching rooms sorted by price");
        List<Room> roomsByFilter = sqlRoomDao.findRoomsSortedByPrice();
        log.debug("Rooms sorted by price: {}", roomsByFilter);
        return roomsByFilter;
    }

    @Override
    public List<Room> getRoomsSortedByType() {
        log.debug("Fetching rooms sorted by type");
        List<Room> roomsByFilter = sqlRoomDao.findRoomsSortedByType();
        log.debug("Rooms sorted by type: {}", roomsByFilter);
        return roomsByFilter;
    }

    @Override
    public List<Booking> getRoomsBookings(Room room) {
        log.debug("Fetching bookings for room with id: {}", room);
        List<Booking> bookings = sqlRoomDao.findRoomBookings(room.getId());
        log.debug("Bookings for room with id {}", room);
        return bookings;
    }

    @Override
    public List<Room> getRooms() {
        log.debug("Fetching all rooms");
        List<Room> rooms = sqlRoomDao.findAll();
        log.debug("Fetched {} all rooms", rooms.size());
        return rooms;
    }

    @Override
    public List<Room> getRooms(RoomFilterRequest filter) {
        return sqlRoomDao.findAll(filter);
    }

    @Override
    public List<Room> getRoomsEager() {
        log.debug("Fetching eager all rooms");
        List<Room> rooms = sqlRoomDao.findAllRoomsEager();
        log.debug("Fetched eager{} all rooms", rooms.size());
        return rooms;
    }

    @Override
    public Long countAll() {
        return sqlRoomDao.countAll();
    }
}
