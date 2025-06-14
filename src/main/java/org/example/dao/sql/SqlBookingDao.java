package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.example.dao.BookingDao;
import org.example.model.domain.Booking;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlBookingDao implements BookingDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        try {
            return Optional.of(entityManager.createQuery(
                            "SELECT b FROM Booking b JOIN FETCH b.visitor JOIN FETCH b.room WHERE b.id = :id", Booking.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> findAll() {
        return entityManager.createQuery("SELECT b FROM Booking b JOIN FETCH b.visitor JOIN FETCH b.room", Booking.class)
                .getResultList();
    }

    @Override
    public List<Booking> findAll(int pageNumber, int pageSize) {
        return entityManager.createQuery("SELECT b FROM Booking b JOIN FETCH b.visitor JOIN FETCH b.room", Booking.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return ((Long) entityManager.createQuery("SELECT COUNT(b) FROM Booking b")
                .getSingleResult());
    }

    @Override
    public Booking save(Booking entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void saveAll(List<Booking> bookings) {
        for (Booking booking : bookings) {
            entityManager.persist(booking);
        }
    }

    @Override
    public void update(Booking entity) {
        throw new NotImplementedException("Booking update operation not implemented");
    }

    @Override
    public void delete(Booking booking) {
        throw new NotImplementedException("Booking delete operation not implemented");
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Booking").executeUpdate();
    }
}
