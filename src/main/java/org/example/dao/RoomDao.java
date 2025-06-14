package org.example.dao;

import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.enums.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomDao {
    Room findById(Long id);

    List<Room> findAll();

    List<Room> findAll(RoomFilterRequest filter);

    Long countAll();

    List<Room> findAllRoomsEager();

    Room save(Room entity);

    void saveAll(List<Room> rooms);

    void update(Room entity);

    void delete(Room room);

    void deleteAll();

    Optional<Room> findByNumber(String number);

    List<Room> findFreeRoomsByType(RoomType type);

    List<Room> findFreeRooms();

    List<Room> findRoomsSortedByPrice();

    List<Room> findRoomsSortedByType();

    List<Booking> findRoomBookings(Long id);
}
