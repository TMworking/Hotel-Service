package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.example.dao.RoomDao;
import org.example.dao.specification.RoomSpecification;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlRoomDao implements RoomDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Room findById(Long id) {
        return entityManager.find(Room.class, id);
    }

    @Override
    public List<Room> findAll() {
        return entityManager.createQuery("FROM " + Room.class.getName(), Room.class)
                .getResultList();
    }

    @Override
    public List<Room> findAll(RoomFilterRequest filter) {
        return RoomSpecification.getQuery(entityManager, filter)
                .setFirstResult(filter.getPage())
                .setMaxResults(filter.getSize())
                .getResultList();
    }

    @Override
    public Long countAll() {
        return ((Long) entityManager.createQuery("SELECT COUNT(r) FROM Room r")
                .getSingleResult());
    }

    @Override
    public List<Room> findAllRoomsEager() {
        return entityManager.createQuery(
                        "SELECT r FROM Room r " +
                                "LEFT JOIN r.bookings b " +
                                "LEFT JOIN b.visitor v " +
                                "LEFT JOIN v.facilityOrderList fo " +
                                "LEFT JOIN fo.facility f", Room.class)
                .getResultList();
    }

    @Override
    public Room save(Room entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void saveAll(List<Room> rooms) {
        for (Room room : rooms) {
            entityManager.persist(room);
        }
    }

    @Override
    public void update(Room entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(Room room) {
        throw new NotImplementedException("Room delete operation not implemented");
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Room").executeUpdate();
    }

    @Override
    public Optional<Room> findByNumber(String number) {
        try {
            return Optional.of(entityManager.createQuery(
                            "FROM Room r WHERE r.roomNumber = :number", Room.class)
                    .setParameter("number", number)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Room> findFreeRoomsByType(RoomType type) {
        return entityManager.createQuery(
                        "FROM Room r WHERE r.status = :status AND r.roomType = :type", Room.class)
                .setParameter("status", RoomStatus.FREE)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public List<Room> findFreeRooms() {
        return entityManager.createQuery("FROM Room r WHERE r.status = :status", Room.class)
                .setParameter("status", RoomStatus.FREE)
                .getResultList();
    }

    @Override
    public List<Room> findRoomsSortedByPrice() {
        return entityManager.createQuery("FROM Room r ORDER BY r.price", Room.class)
                .getResultList();
    }

    @Override
    public List<Room> findRoomsSortedByType() {
        return entityManager.createQuery("FROM Room r ORDER BY r.roomType", Room.class)
                .getResultList();
    }

    @Override
    public List<Booking> findRoomBookings(Long id) {
        return entityManager.createQuery("FROM Booking b WHERE b.room.id = :roomId", Booking.class)
                .setParameter("roomId", id)
                .getResultList();
    }
}
