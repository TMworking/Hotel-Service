package org.example.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Room implements Exportable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "room_number", unique = true)
    private String roomNumber;

    @NonNull
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoomStatus status = RoomStatus.FREE;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    public int getMaxVisitors() {
        return roomType.getMaxVisitors();
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.setRoom(this);
    }

    @Override
    public String toCsvString() {
        return roomNumber + "," +
                roomType + "," +
                price + "," +
                status;
    }

    @Override
    public String csvHeader() {
        return "room_number,room_type,price,status";
    }
}
