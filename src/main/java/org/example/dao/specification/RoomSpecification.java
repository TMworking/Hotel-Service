package org.example.dao.specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static TypedQuery<Room> getQuery(EntityManager entityManager, RoomFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> query = cb.createQuery(Room.class);
        Root<Room> root = query.from(Room.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getSettleDate() != null && filter.getDuration() != null) {
            Predicate availableRooms = getAvailableRooms(cb, root, filter.getSettleDate(), filter.getDuration());
            predicates.add(availableRooms);
        }

        byFilters(filter, predicates, cb, root);
        bySort(filter, root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query);
    }

    private static void bySort(RoomFilterRequest filter, Root<Room> root, CriteriaQuery<Room> query, CriteriaBuilder cb) {
        if (filter.getSortBy() != null) {
            Path<Object> sortField = root.get(filter.getSortBy());
            if ("ASC".equalsIgnoreCase(filter.getSortDirection())) {
                query.orderBy(cb.asc(sortField));
            } else {
                query.orderBy(cb.desc(sortField));
            }
        }
    }

    private static void byFilters(RoomFilterRequest filter, List<Predicate> predicates, CriteriaBuilder cb, Root<Room> root) {
        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getRoomType() != null) {
            predicates.add(cb.equal(root.get("roomType"), filter.getRoomType()));
        }

        if (filter.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }
    }

    private static Predicate getAvailableRooms(CriteriaBuilder cb, Root<Room> root, LocalDateTime settleDate, Integer duration) {
        Join<Room, Booking> bookings = root.join("bookings", JoinType.LEFT);

        LocalDateTime endDate = settleDate.plusDays(duration);

        Expression<LocalDateTime> bookingEnd = cb.sum(
                bookings.get("settleDate"),
                bookings.get("duration")
        ).as(LocalDateTime.class);

        Predicate noOverlap = cb.or(
                cb.greaterThanOrEqualTo(bookings.get("settleDate"), endDate),
                cb.lessThanOrEqualTo(bookingEnd, settleDate)
        );

        return cb.or(
                cb.isEmpty(root.get("bookings")),
                noOverlap
        );
    }
}
