package org.example.service.domain;

import org.example.service.RoomProperties;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    RoomProperties getRoomProperties();

    List<Visitor> getLastRoomVisitors(Room room);

    void changeRoomStatus(Long id, RoomStatus status);

    Room getById(Long id) throws EntityNotFoundException;

    Room getByNumber(String number) throws EntityNotFoundException;

    Room saveRoom(String roomNumber, BigDecimal price, RoomType type);

    void saveAllRooms(List<Room> rooms);

    void updateRoom(Room room);

    void deleteAllRooms();

    List<Room> getFreeRooms();

    LocalDateTime getRoomFreeDate(Room room);

    List<Room> getFreeRoomsByDate(LocalDateTime date);

    List<Room> getFreeRoomsByType(RoomType roomType);

    List<Room> getRoomsSortedByPrice();

    List<Room> getRoomsSortedByType();

    List<Booking> getRoomsBookings(Room room);

    List<Room> getRooms();

    List<Room> getRooms(RoomFilterRequest filter);

    List<Room> getRoomsEager();

    Long countAll();
}
