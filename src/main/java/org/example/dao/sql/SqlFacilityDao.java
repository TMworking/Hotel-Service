package org.example.dao.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.example.dao.FacilityDao;
import org.example.model.domain.Facility;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SqlFacilityDao implements FacilityDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Facility findById(Long id) {
        return entityManager.find(Facility.class, id);
    }

    @Override
    public List<Facility> findAll() {
        return entityManager.createQuery("FROM " + Facility.class.getName(), Facility.class)
                .getResultList();
    }

    @Override
    public List<Facility> findAll(int pageNumber, int pageSize) {
        return entityManager.createQuery("FROM " + Facility.class.getName(), Facility.class)
                .setMaxResults(pageSize)
                .setFirstResult(pageNumber * pageSize)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return ((Long) entityManager.createQuery("SELECT COUNT(f) FROM Facility f")
                .getSingleResult());
    }

    @Override
    public Facility save(Facility entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void saveAll(List<Facility> facilities) {
        for (Facility facility : facilities) {
            entityManager.persist(facility);
        }
    }

    @Override
    public void update(Facility entity) {
        throw new NotImplementedException("Facility update operation not implemented");
    }

    @Override
    public void delete(Facility facility) {
        throw new NotImplementedException("Facility delete operation not implemented");
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Facility").executeUpdate();
    }

    @Override
    public List<Facility> findFacilitiesSortedByCost() {
        return entityManager.createQuery("FROM Facility f ORDER BY f.cost", Facility.class)
                .getResultList();
    }
}
