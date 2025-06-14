package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.example.dao.FacilityOrderDao;
import org.example.model.domain.FacilityOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlFacilityOrderDao implements FacilityOrderDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<FacilityOrder> findById(Long id) {
        try {
            return Optional.of(entityManager.createQuery("SELECT fo FROM FacilityOrder fo JOIN FETCH fo.facility JOIN FETCH fo.visitor WHERE fo.id = :id", FacilityOrder.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<FacilityOrder> findAll() {
        return entityManager.createQuery("SELECT fo FROM FacilityOrder fo JOIN FETCH fo.facility JOIN FETCH fo.visitor", FacilityOrder.class)
                .getResultList();
    }

    @Override
    public List<FacilityOrder> findAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FacilityOrder> query = cb.createQuery(FacilityOrder.class);
        Root<FacilityOrder> root = query.from(FacilityOrder.class);

        List<Order> orders = new ArrayList<>();
        Path<Object> field = root.get(sortBy);
        if ("asc".equalsIgnoreCase(sortDirection)) {
            orders.add(cb.asc(field));
        } else {
            orders.add(cb.desc(field));
        }
        query.orderBy(orders);

        TypedQuery<FacilityOrder> typedQuery = entityManager.createQuery(query);

        return typedQuery
                .setMaxResults(pageSize)
                .setFirstResult(pageNumber * pageSize)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return ((Long) entityManager.createQuery("SELECT COUNT(fo) FROM FacilityOrder fo")
                .getSingleResult());
    }

    @Override
    public FacilityOrder save(FacilityOrder entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void saveAll(List<FacilityOrder> facilityOrders) {
        for (FacilityOrder facilityOrder : facilityOrders) {
            entityManager.persist(facilityOrder);
        }
    }

    @Override
    public void update(FacilityOrder entity) {
        throw new NotImplementedException("Facility order update operation not implemented");
    }

    @Override
    public void delete(FacilityOrder facilityOrder) {
        throw new NotImplementedException("Facility order delete operation not implemented");
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM FacilityOrder").executeUpdate();
    }

    @Override
    public List<FacilityOrder> findFacilityOrdersSortedByDate() {
        return entityManager.createQuery("FROM FacilityOrder fo ORDER BY fo.date", FacilityOrder.class)
                .getResultList();
    }
}
