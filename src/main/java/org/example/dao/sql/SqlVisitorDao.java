package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.example.dao.VisitorDao;
import org.example.model.domain.Booking;
import org.example.model.domain.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SqlVisitorDao implements VisitorDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Visitor findById(Long id) {
        return entityManager.find(Visitor.class, id);
    }

    @Override
    public List<Visitor> findAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Visitor> query = cb.createQuery(Visitor.class);
        Root<Visitor> root = query.from(Visitor.class);

        List<Order> orders = new ArrayList<>();
        Path<Object> field = root.get(sortBy);
        if ("asc".equalsIgnoreCase(sortDirection)) {
            orders.add(cb.asc(field));
        } else {
            orders.add(cb.desc(field));
        }
        query.orderBy(orders);

        TypedQuery<Visitor> typedQuery = entityManager.createQuery(query);

        return typedQuery
                .setMaxResults(pageSize)
                .setFirstResult(pageNumber * pageSize)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return ((Long) entityManager.createQuery("SELECT COUNT(v) FROM Visitor v")
                .getSingleResult());
    }

    @Override
    public List<Visitor> findAll() {
        return entityManager.createQuery("FROM " + Visitor.class.getName(), Visitor.class)
                .getResultList();
    }

    @Override
    public Visitor save(Visitor visitor) {
        entityManager.persist(visitor);
        return visitor;
    }

    @Override
    public void saveAll(List<Visitor> visitors) {
        for (Visitor visitor : visitors) {
            entityManager.persist(visitor);
        }
    }

    @Override
    public void update(Visitor visitor) {
        throw new NotImplementedException("Visitor update operation not implemented");
    }

    @Override
    public void delete(Visitor visitor) {
        throw new NotImplementedException("Visitor delete operation not implemented");
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Visitor").executeUpdate();
    }

    @Override
    public List<Visitor> findVisitorsSortedByName() {
        return entityManager.createQuery("FROM Visitor v ORDER BY v.name", Visitor.class)
                .getResultList();
    }

    @Override
    public List<Visitor> findVisitorsSortedBySurname() {
        return entityManager.createQuery("FROM Visitor v ORDER BY v.surname", Visitor.class)
                .getResultList();
    }

    @Override
    public List<Booking> findVisitorsBookings(Visitor visitor) {
        return entityManager.createQuery("FROM Booking b WHERE b.visitor.id = :visitorId", Booking.class)
                .setParameter("visitorId", visitor.getId())
                .getResultList();
    }
}
